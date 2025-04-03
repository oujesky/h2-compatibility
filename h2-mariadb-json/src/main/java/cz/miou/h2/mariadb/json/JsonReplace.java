package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonModifyUtil.jsonModify;

/**
 * <a href="https://mariadb.com/kb/en/json_replace/">JSON_REPLACE</a>
 */
public class JsonReplace implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_REPLACE";
    }

    @Override
    public String getMethodName() {
        return "jsonReplace";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonReplace(Value json, Value... values) {
        return jsonModify(json, values, node -> true, true);
    }
    
}