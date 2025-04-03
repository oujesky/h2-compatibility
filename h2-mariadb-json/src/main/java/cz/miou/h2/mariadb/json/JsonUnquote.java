package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;

import java.util.regex.Pattern;

/**
 * <a href="https://mariadb.com/kb/en/json_unquote/">JSON_UNQUOTE</a>
 */
public class JsonUnquote implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_UNQUOTE";
    }

    @Override
    public String getMethodName() {
        return "jsonUnquote";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    private static final Pattern UNICODE_PATTERN = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
    
    @SuppressWarnings("unused")
    public static String jsonUnquote(String val) {
        if (val == null) {
            return null;
        }

        if (val.startsWith("\"") && val.endsWith("\"")) {
            val = val.substring(1, val.length() - 1)
                .replace("\\\\", "\\");
        }

        return UNICODE_PATTERN.matcher(val)
            .replaceAll(match -> {
                var codePoint = Integer.parseInt(match.group(1), 16);
                return Character.toString(codePoint);
            })
            .replace("\\\"", "\"")
            .replace("\\b", "\b")
            .replace("\\f", "\f")
            .replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\t", "\t")
            .replace("\\\\", "\\");
    }
    
}