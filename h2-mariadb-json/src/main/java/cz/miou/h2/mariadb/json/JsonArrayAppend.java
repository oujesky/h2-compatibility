package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static cz.miou.h2.mariadb.json.JsonModifyUtil.jsonModify;
import static cz.miou.h2.mariadb.json.JsonUtil.scalarValueToJsonNode;

/**
 * <a href="https://mariadb.com/kb/en/json_array_append/">JSON_ARRAY_APPEND</a>
 */
public class JsonArrayAppend implements FunctionDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(JsonArrayAppend.class);

    @Override
    public String getName() {
        return "JSON_ARRAY_APPEND";
    }

    @Override
    public String getMethodName() {
        return "jsonArrayAppend";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value jsonArrayAppend(Value json, Value... values) {
        return jsonModify(json, values, JsonArrayAppend::modify);
    }

    private static DocumentContext modify(DocumentContext document, JsonPath path, Value value) {
        var node = document.read(path, JsonNode.class);
        if (node == null) {
            return null;
        }

        try {
            if (!node.isArray()) {
                if (JsonUtil.isRootPath(path)) {
                    document = JsonUtil.createJsonArrayDocument();
                    document.add(path, node);
                } else {
                    document.set(path, List.of(node));
                }
            }

            document.add(path, scalarValueToJsonNode(value));

        } catch (Exception e) {
            LOG.debug("Failed to append json array", e);
            return null;
        }

        return document;
    }

}