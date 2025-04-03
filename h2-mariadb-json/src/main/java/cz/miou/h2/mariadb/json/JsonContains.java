package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.util.HashSet;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonEquals;
import static cz.miou.h2.mariadb.json.JsonUtil.readJsonNode;

/**
 * <a href="https://mariadb.com/kb/en/json_contains/">JSON_CONTAINS</a>
 */
public class JsonContains implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_CONTAINS";
    }

    @Override
    public String getMethodName() {
        return "jsonContains";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Boolean jsonContains(Value json, String val) {
        return jsonContains(json, val, "$");
    }

    @SuppressWarnings("unused")
    public static Boolean jsonContains(Value json, String value, String path) {
        var candidate = readJsonNode(value);
        if (candidate == null) {
            return null;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return null;
        }

        var targetPath = JsonPath.compile(path);
        if (!targetPath.isDefinite()) {
            return null;
        }

        var target = document.read(targetPath, JsonNode.class);
        if (target == null) {
            return false;
        }

        if (target.isArray()) {
            return containsInArray(target, candidate);
        }

        return jsonEquals(target, candidate);
    }

    private static boolean containsInArray(JsonNode target, JsonNode candidate) {
        var targetValues = new HashSet<>();
        target.iterator().forEachRemaining(targetValues::add);

        if (candidate.isArray()) {
            var candidateValues = new HashSet<>();
            candidate.iterator().forEachRemaining(candidateValues::add);
            return targetValues.containsAll(candidateValues);
        } else {
            return targetValues.contains(candidate);
        }
    }

}