package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;

/**
 * <a href="https://mariadb.com/kb/en/json_equals/">JSON_EQUALS</a>
 */
public class JsonEquals implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_EQUALS";
    }

    @Override
    public String getMethodName() {
        return "jsonEquals";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Boolean jsonEquals(Value json1, Value json2) {
        if (json1 == null || json2 == null) {
            return null;
        }

        var node1 = jsonNodeFromValue(json1);
        var node2 = jsonNodeFromValue(json2);

        if (node1 == null || node2 == null) {
            return null;
        }

        return JsonUtil.jsonEquals(node1, node2);
    }

}