package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonModifyUtil.jsonModify;

/**
 * <a href="https://mariadb.com/kb/en/json_insert/">JSON_INSERT</a>
 */
public class JsonInsert implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_INSERT";
    }

    @Override
    public String getMethodName() {
        return "jsonInsert";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonInsert(Value json, Value... values) {
        return jsonModify(json, values, node -> node == null || node.isMissingNode(), false);
    }

}