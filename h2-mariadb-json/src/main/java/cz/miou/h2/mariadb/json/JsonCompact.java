package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toCompactJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_compact/">JSON_COMPACT</a>
 */
public class JsonCompact implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_COMPACT";
    }

    @Override
    public String getMethodName() {
        return "jsonCompact";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonCompact(Value json) {
        if (json == null) {
            return ValueNull.INSTANCE;
        }

        var node = jsonNodeFromValue(json);
        if (node == null) {
            return ValueNull.INSTANCE;
        }

        return toCompactJsonValue(node);
    }
    
}