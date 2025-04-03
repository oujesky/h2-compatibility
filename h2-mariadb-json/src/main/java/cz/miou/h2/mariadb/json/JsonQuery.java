package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_query/">JSON_QUERY</a>
 */
public class JsonQuery implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_QUERY";
    }

    @Override
    public String getMethodName() {
        return "jsonQuery";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonQuery(Value json, String path) {
        if (json == null || path == null) {
            return ValueNull.INSTANCE;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return ValueNull.INSTANCE;
        }

        return toJsonValue(document.read(path, JsonNode.class));
    }
    
}