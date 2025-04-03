package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonModifyUtil.jsonModify;

/**
 * <a href="https://mariadb.com/kb/en/json_set/">JSON_SET</a>
 */
public class JsonSet implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_SET";
    }

    @Override
    public String getMethodName() {
        return "jsonSet";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonSet(Value json, Value... values) {
        return jsonModify(json, values, node -> true, false);
    }
    
}