package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.valueFromJsonNode;

/**
 * <a href="https://mariadb.com/kb/en/json_value/">JSON_VALUE</a>
 */
public class JsonValue implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_VALUE";
    }

    @Override
    public String getMethodName() {
        return "jsonValue";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonValue(Value json, String path) {
        if (json == null || path == null) {
            return ValueNull.INSTANCE;
        }

        var jsonPath = JsonPath.compile(path);
        if (!jsonPath.isDefinite()) {
            return ValueNull.INSTANCE;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return ValueNull.INSTANCE;
        }

        var node = document.read(path, JsonNode.class);
        if (node == null || node.isNull() || !node.isValueNode()) {
            return ValueNull.INSTANCE;
        }

        return valueFromJsonNode(node);
    }
    
}