package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;

/**
 * <a href="https://mariadb.com/kb/en/json_type/">JSON_TYPE</a>
 */
public class JsonType implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_TYPE";
    }

    @Override
    public String getMethodName() {
        return "jsonType";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static String jsonType(Value json) {
        if (json == null) {
            return null;
        }

        var node = jsonNodeFromValue(json);
        if (node == null) {
            return null;
        }

        if (node.getNodeType() == JsonNodeType.NUMBER) {
            return node.isFloatingPointNumber() ? "DOUBLE" : "INTEGER";
        }

        return node.getNodeType().name();
    }
    
}