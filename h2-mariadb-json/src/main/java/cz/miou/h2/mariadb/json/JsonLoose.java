package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import static cz.miou.h2.mariadb.json.JsonUtil.readJsonNode;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_loose/">JSON_LOOSE</a>
 */
public class JsonLoose implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_LOOSE";
    }

    @Override
    public String getMethodName() {
        return "jsonLoose";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonLoose(String json) {
        if (json == null) {
            return ValueNull.INSTANCE;
        }

        var node = readJsonNode(json);
        if (node == null) {
            return ValueNull.INSTANCE;
        }

        return toJsonValue(node);
    }
    
}