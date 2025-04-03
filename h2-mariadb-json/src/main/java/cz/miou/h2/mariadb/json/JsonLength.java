package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;

/**
 * <a href="https://mariadb.com/kb/en/json_length/">JSON_LENGTH</a>
 */
public class JsonLength implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_LENGTH";
    }

    @Override
    public String getMethodName() {
        return "jsonLength";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Integer jsonLength(Value json) {
        return jsonLength(json, "$");
    }

    @SuppressWarnings("unused")
    public static Integer jsonLength(Value json, String path) {
        if (json == null || path == null) {
            return null;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return null;
        }

        var node = document.read(path, JsonNode.class);
        if (node == null || node.isMissingNode()) {
            return null;
        }

        if (node.isObject() || node.isArray()) {
            return node.size();
        }

        return 1;
    }

}