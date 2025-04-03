package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonPathExists;

/**
 * <a href="https://mariadb.com/kb/en/json_exists/">JSON_EXISTS</a>
 */
public class JsonExists implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_EXISTS";
    }

    @Override
    public String getMethodName() {
        return "jsonExists";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Boolean jsonExists(Value json, String path) {
        if (json == null || path == null) {
            return null;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return null;
        }

        return jsonPathExists(document, path);
    }
    
}