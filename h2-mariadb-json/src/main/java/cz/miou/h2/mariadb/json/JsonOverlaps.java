package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonEquals;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;

/**
 * <a href="https://mariadb.com/kb/en/json_overlaps/">JSON_OVERLAPS</a>
 */
public class JsonOverlaps implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_OVERLAPS";
    }

    @Override
    public String getMethodName() {
        return "jsonOverlaps";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Boolean jsonOverlaps(Value json1, Value json2) {
        if (json1 == null || json2 == null) {
            return null;
        }

        var node1 = jsonNodeFromValue(json1);
        if (node1 == null) {
            return null;
        }

        var node2 = jsonNodeFromValue(json2);
        if (node2 == null) {
            return null;
        }

        if (node1.isObject() && node2.isObject()) {
            return objectsOverlaps(node1, node2);
        }

        if (node1.isArray() && node2.isArray()) {
            return arraysOverlaps(node1, node2);
        }

        if (node1.isArray()) {
            return arrayContains(node1, node2);
        }

        if (node2.isArray()) {
            return arrayContains(node2, node1);
        }


        return jsonEquals(node1, node2);
    }

    private static boolean arraysOverlaps(JsonNode node1, JsonNode node2) {
        var it = node1.elements();
        while (it.hasNext()) {
            var element = it.next();
            if (arrayContains(node2, element)) {
                return true;
            }
        }
        return false;
    }

    private static boolean objectsOverlaps(JsonNode node1, JsonNode node2) {
        var it = node1.fields();
        while (it.hasNext()) {
            var entry = it.next();
            var key = entry.getKey();
            if (node2.has(key) && jsonEquals(entry.getValue(), node2.get(key))) {
                return true;
            }
        }
        return false;
    }

    private static boolean arrayContains(JsonNode array, JsonNode node) {
        var it = array.elements();
        while (it.hasNext()) {
            if (jsonEquals(it.next(), node)) {
                return true;
            }
        }
        return false;
    }

}