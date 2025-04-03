package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_object_filter_keys/">JSON_OBJECT_FILTER_KEYS</a>
 */
public class JsonObjectFilterKeys implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_OBJECT_FILTER_KEYS";
    }

    @Override
    public String getMethodName() {
        return "jsonObjectFilterKeys";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonObjectFilterKeys(Value json, Value keys) {
        if (json == null || keys == null) {
            return ValueNull.INSTANCE;
        }

        var objectNode = jsonNodeFromValue(json);
        if (objectNode == null || !objectNode.isObject()) {
            return ValueNull.INSTANCE;
        }

        var keysNode = jsonNodeFromValue(keys);
        if (keysNode == null || !keysNode.isArray()) {
            return ValueNull.INSTANCE;
        }

        var result = JsonNodeFactory.instance.objectNode();
        keysNode.elements().forEachRemaining(element -> {
            if (element.isTextual()) {
                var key = element.asText();
                if (objectNode.has(key)) {
                    result.set(key, objectNode.get(key).deepCopy());
                }
            }
        });

        return result.isEmpty()
            ? ValueNull.INSTANCE
            : toJsonValue(result);
    }

}