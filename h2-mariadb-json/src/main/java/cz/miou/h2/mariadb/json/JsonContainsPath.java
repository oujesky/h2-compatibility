package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import cz.miou.h2.mariadb.json.JsonUtil.OneOrAll;
import org.h2.value.Value;

import java.util.Arrays;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonPathExists;

/**
 * <a href="https://mariadb.com/kb/en/json_contains_path/">JSON_CONTAINS_PATH</a>
 */
public class JsonContainsPath implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_CONTAINS_PATH";
    }

    @Override
    public String getMethodName() {
        return "jsonContainsPath";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Boolean jsonContainsPath(Value json, String returnArg, String... paths) {
        if (json == null || returnArg == null || paths == null || paths.length == 0) {
            return null;
        }

        var mode = OneOrAll.fromString(returnArg);
        if (mode == null) {
            return null;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return null;
        }

        switch (mode) {
            case ALL:
                return Arrays.stream(paths).allMatch(path -> jsonPathExists(document, path));
            case ONE:
                return Arrays.stream(paths).anyMatch(path -> jsonPathExists(document, path));
            default:
                return null;
        }
    }

}