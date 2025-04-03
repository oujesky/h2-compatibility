package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toDetailedJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_detailed/">JSON_DETAILED</a>
 */
public class JsonDetailed implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_DETAILED";
    }

    @Override
    public String getMethodName() {
        return "jsonDetailed";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonDetailed(Value json, Integer tabSize) {
        if (json == null || tabSize == null) {
            return ValueNull.INSTANCE;
        }

        var node = jsonNodeFromValue(json);
        if (node == null) {
            return ValueNull.INSTANCE;
        }

        return toDetailedJsonValue(node, tabSize);
    }

    @SuppressWarnings("unused")
    public static Value jsonDetailed(Value json) {
        return jsonDetailed(json, 4);
    }

}