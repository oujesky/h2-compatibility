package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_object_to_array/">JSON_OBJECT_TO_ARRAY</a>
 */
public class JsonObjectToArray implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_OBJECT_TO_ARRAY";
    }

    @Override
    public String getMethodName() {
        return "jsonObjectToArray";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonObjectToArray(Value json) {
        if (json == null) {
            return ValueNull.INSTANCE;
        }

        var node = jsonNodeFromValue(json);
        if (node == null || !node.isObject() || node.isEmpty()) {
            return ValueNull.INSTANCE;
        }

        var result = JsonNodeFactory.instance.arrayNode();
        node.fields().forEachRemaining(entry -> {
            var transformed = JsonNodeFactory.instance.arrayNode()
                .add(entry.getKey())
                .add(entry.getValue());
            result.add(transformed);
        });

        return toJsonValue(result);
    }
    
}