package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import cz.miou.h2.api.FunctionDefinition;
import cz.miou.h2.mariadb.json.JsonUtil.OneOrAll;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static cz.miou.h2.mariadb.json.JsonUtil.getJsonPathConfiguration;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_search/">JSON_SEARCH</a>
 * <p>
 * Differences to MariaDB:
 * <ul>
 *   <li>Doesn't handle '**' wildcards
 * </ul>
 */
public class JsonSearch implements FunctionDefinition {

    private static final String DEFAULT_ESCAPE_CHAR = "\\";
    private static final String[] DEFAULT_PATH = { "$" };

    private static Configuration pathListConfiguration;

    @Override
    public String getName() {
        return "JSON_SEARCH";
    }

    @Override
    public String getMethodName() {
        return "jsonSearch";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value jsonSearch(Value json, String returnArg, String searchStr) {
        return jsonSearch(json, returnArg, searchStr, null);
    }

    @SuppressWarnings("unused")
    public static Value jsonSearch(Value json, String returnArg, String searchStr, String escapeChar, String... paths) {
        if (json == null || returnArg == null || searchStr == null || paths == null) {
            return ValueNull.INSTANCE;
        }

        if (paths.length == 0) {
            paths = DEFAULT_PATH;
        }

        if (escapeChar == null || escapeChar.isEmpty()) {
            escapeChar = DEFAULT_ESCAPE_CHAR;
        }

        if (escapeChar.length() > 1) {
            return ValueNull.INSTANCE;
        }

        var mode = OneOrAll.fromString(returnArg);
        if (mode == null) {
            return ValueNull.INSTANCE;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return ValueNull.INSTANCE;
        }
        var documentNode = (JsonNode) document.json();

        var matcher = createMatcher(searchStr, escapeChar);
        var configuration = getPathListConfiguration();

        var result = new LinkedHashSet<String>();

        for (var path : paths) {

            var roots = JsonPath.compile(path).read(documentNode, configuration);
            if (!(roots instanceof ArrayNode)) {
                continue;
            }

            var rootsArray = (ArrayNode) roots;
            for (var root : rootsArray) {
                var rootPath = root.asText();

                var rootNode = document.read(rootPath, JsonNode.class);
                if (rootNode != null && rootNode.isValueNode() && matcher.test(rootNode.asText())) {
                    result.add(normalizePath(rootPath));
                    if (mode == OneOrAll.ONE) {
                        return wrapResult(result);
                    }
                }

                // json-path library doesn't traverse on value nodes from object fields, we need to check fields in second pass
                var firstPassPath = JsonPath.compile(rootPath + "..[?]", ctx -> allowMatchOrObject(ctx, matcher));

                var firstPass = firstPassPath.read(documentNode, configuration);
                if (!(firstPass instanceof ArrayNode)) {
                    continue;
                }

                var firstPassArray = (ArrayNode) firstPass;
                for (var item : firstPassArray) {
                    var candidatePath = item.asText();
                    var candidateNode = document.read(candidatePath, JsonNode.class);

                    // this is already a match from the first pass
                    if (candidateNode.isValueNode()) {
                        result.add(normalizePath(candidatePath));
                        if (mode == OneOrAll.ONE) {
                            return wrapResult(result);
                        }
                    }

                    // object fields needs to be traversed and checked
                    if (candidateNode.isObject()) {
                        var it = candidateNode.fields();
                        while (it.hasNext()) {
                            var entry = it.next();
                            var value = entry.getValue();

                            if (!value.isValueNode() || !matcher.test(value.asText())) {
                                continue;
                            }

                            var fieldName = entry.getKey();
                            var valuePath = String.format("%s.%s", normalizePath(candidatePath), fieldName);
                            result.add(valuePath);
                            if (mode == OneOrAll.ONE) {
                                return wrapResult(result);
                            }
                        }
                    }
                }
            }
        }

        return wrapResult(result);
    }

    private static final Pattern MATCHER_TOKEN = Pattern.compile("(?=(?<!\\\\)[_%])|(?<=(?<!\\\\)[_%])");

    private static Predicate<String> createMatcher(String searchStr, String escapeChar) {
        var splitter = DEFAULT_ESCAPE_CHAR.equals(escapeChar)
            ? MATCHER_TOKEN
            : Pattern.compile("(?=(?<!" + Pattern.quote(escapeChar) + ")[_%])|(?<=(?<!" + Pattern.quote(escapeChar) + ")[_%])");

        var tokens = splitter.split(searchStr);
        if (tokens.length == 0) {
            return searchStr::equals;
        }

        var sb = new StringBuilder();
        sb.append('^');
        for (var token : tokens) {
            switch (token) {
                case "%":
                    sb.append(".*");
                    break;
                case "_":
                    sb.append(".");
                    break;
                default:
                    var escaped = token.replace(escapeChar + "%", "%")
                            .replace(escapeChar + "_", "_");
                    sb.append(Pattern.quote(escaped));
            }
        }
        sb.append('$');

        var pattern = Pattern.compile(sb.toString());

        return str -> pattern.matcher(str).matches();
    }

    private static boolean allowMatchOrObject(com.jayway.jsonpath.Predicate.PredicateContext ctx, Predicate<String> matcher) {
        var item = ctx.item();

        if (item instanceof JsonNode) {
            var node = (JsonNode) item;
            return node.isContainerNode() && !node.isEmpty();
        }

        return matcher.test(item.toString());
    }

    private static Value wrapResult(Set<String> result) {
        switch (result.size()) {
            case 0:
                return ValueNull.INSTANCE;
            case 1:
                return toJsonValue(result.iterator().next());
            default:
                return toJsonValue(result);
        }
    }

    private static Configuration getPathListConfiguration() {
        if (pathListConfiguration == null) {
            pathListConfiguration = getJsonPathConfiguration()
                .addOptions(Option.AS_PATH_LIST);
        }
        return pathListConfiguration;
    }

    private static String normalizePath(String path) {
        return path.replace("['", ".")
            .replace("']", "");
    }

}