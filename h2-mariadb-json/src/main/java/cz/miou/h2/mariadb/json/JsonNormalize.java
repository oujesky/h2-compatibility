package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.util.Map;

import static cz.miou.h2.mariadb.json.JsonUtil.getEntries;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toCompactJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_normalize/">JSON_NORMALIZE</a>
 */
public class JsonNormalize implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_NORMALIZE";
    }

    @Override
    public String getMethodName() {
        return "jsonNormalize";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonNormalize(Value json) {
        if (json == null) {
            return null;
        }

        var node = jsonNodeFromValue(json);
        if (node == null) {
            return null;
        }

        node = sortFields(node);

        return toCompactJsonValue(node);
    }

    private static JsonNode sortFields(JsonNode node) {
        if (node.isObject() && !node.isEmpty()) {
            var sorted = JsonNodeFactory.instance.objectNode();
            getEntries(node)
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sorted.set(entry.getKey(), sortFields(entry.getValue())));
            return sorted;
        }

        if (node.isArray() && !node.isEmpty()) {
            var sorted = JsonNodeFactory.instance.arrayNode();
            node.elements().forEachRemaining(item -> sorted.add(sortFields(item)));
            return sorted;
        }

        return node;
    }

}