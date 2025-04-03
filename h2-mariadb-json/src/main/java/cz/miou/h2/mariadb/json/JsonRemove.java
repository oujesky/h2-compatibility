package cz.miou.h2.mariadb.json;

import com.jayway.jsonpath.JsonPath;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import static cz.miou.h2.mariadb.json.JsonUtil.isRootPath;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonPathExists;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_remove/">JSON_REMOVE</a>
 */
public class JsonRemove implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_REMOVE";
    }

    @Override
    public String getMethodName() {
        return "jsonRemove";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonRemove(Value json, String... paths) {
        if (json == null || paths == null || paths.length == 0) {
            return ValueNull.INSTANCE;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return ValueNull.INSTANCE;
        }

        for (var path : paths) {
            var jsonPath = JsonPath.compile(path);
            if (isRootPath(jsonPath) || !jsonPath.isDefinite()) {
                return ValueNull.INSTANCE;
            }

            if (jsonPathExists(document, jsonPath)) {
                document.delete(jsonPath);
            }
        }

        return toJsonValue(document);
    }
    
}