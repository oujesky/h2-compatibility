package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import java.util.Map;
import java.util.stream.Collectors;

import static cz.miou.h2.mariadb.json.JsonUtil.getEntries;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_key_value/">JSON_KEY_VALUE</a>
 */
public class JsonKeyValue implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_KEY_VALUE";
    }

    @Override
    public String getMethodName() {
        return "jsonKeyValue";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonKeyValue(Value json, String path) {
        if (json == null || path == null) {
            return ValueNull.INSTANCE;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return ValueNull.INSTANCE;
        }

        var node = document.read(path, JsonNode.class);
        if (node == null || node.isMissingNode() || !node.isObject()) {
            return ValueNull.INSTANCE;
        }

        var result = getEntries(node)
            .map(entry -> Map.of("key", entry.getKey(), "value", entry.getValue()))
            .collect(Collectors.toUnmodifiableList());

        return toJsonValue(result);
    }

}