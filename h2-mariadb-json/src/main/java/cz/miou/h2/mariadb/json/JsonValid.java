package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;

import static cz.miou.h2.mariadb.json.JsonUtil.readJsonNode;

/**
 * <a href="https://mariadb.com/kb/en/json_valid/">JSON_VALID</a>
 */
public class JsonValid implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_VALID";
    }

    @Override
    public String getMethodName() {
        return "jsonValid";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Boolean jsonValid(String value) {
        if (value == null) {
            return null;
        }

        return readJsonNode(value) != null;
    }

}