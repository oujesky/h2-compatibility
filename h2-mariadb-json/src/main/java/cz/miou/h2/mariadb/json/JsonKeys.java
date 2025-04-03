package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_keys/">JSON_KEYS</a>
 */
public class JsonKeys implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_KEYS";
    }

    @Override
    public String getMethodName() {
        return "jsonKeys";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value jsonKeys(Value json) {
        return jsonKeys(json, "$");
    }
    
    @SuppressWarnings("unused")
    public static Value jsonKeys(Value json, String path) {
        if (json == null || path == null) {
            return ValueNull.INSTANCE;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return ValueNull.INSTANCE;
        }

        var node = document.read(path, JsonNode.class);
        if (node == null || node.isMissingNode() || !node.isObject()) {
            return ValueNull.INSTANCE;
        }

        return toJsonValue(node.fieldNames());
    }
    
}