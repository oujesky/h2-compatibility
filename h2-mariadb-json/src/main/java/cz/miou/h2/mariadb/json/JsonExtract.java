package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import java.util.ArrayList;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_extract/">JSON_EXTRACT</a>
 */
public class JsonExtract implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_EXTRACT";
    }

    @Override
    public String getMethodName() {
        return "jsonExtract";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonExtract(Value json, String... paths) {
        if (json == null || paths == null || paths.length == 0) {
            return ValueNull.INSTANCE;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return ValueNull.INSTANCE;
        }

        var result = new ArrayList<JsonNode>();

        for (var path : paths) {
            var node = document.read(path, JsonNode.class);
            if (node != null && !node.isMissingNode()) {
                result.add(node);
            }
        }

        if (result.isEmpty()) {
            return ValueNull.INSTANCE;
        }

        if (result.size() == 1) {
            return toJsonValue(result.get(0));
        }

        return toJsonValue(result);
    }
    
}