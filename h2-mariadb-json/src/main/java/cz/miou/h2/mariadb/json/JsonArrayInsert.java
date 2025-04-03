package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import cz.miou.h2.api.FunctionDefinition;
import cz.miou.h2.mariadb.json.JsonUtil.ArrayPathWithParent;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonModifyUtil.jsonModify;
import static cz.miou.h2.mariadb.json.JsonUtil.getPathWithParent;
import static cz.miou.h2.mariadb.json.JsonUtil.scalarValueToJsonNode;

/**
 * <a href="https://mariadb.com/kb/en/json_array_insert/">JSON_ARRAY_INSERT</a>
 */
public class JsonArrayInsert implements FunctionDefinition {

    @Override
    public String getName() {
        return "JSON_ARRAY_INSERT";
    }

    @Override
    public String getMethodName() {
        return "jsonArrayInsert";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value jsonArrayInsert(Value json, Value... values) {
        return jsonModify(json, values, JsonArrayInsert::modify);
    }

    private static DocumentContext modify(DocumentContext document, JsonPath path, Value value) {
        var pathWithParent = getPathWithParent(path);
        if (!(pathWithParent instanceof ArrayPathWithParent)) {
            return null;
        }

        var arrayPath = (ArrayPathWithParent) pathWithParent;
        var index = arrayPath.getIndex();

        var parentNode = document.read(arrayPath.getParentPath(), JsonNode.class);
        if (!parentNode.isArray()) {
            return document;
        }

        var array = (ArrayNode) parentNode;

        if (index < 0) {
            if (-index > array.size() + 1) {
                index = array.size();
            } else {
                index = array.size() + 1 + index;
            }
        }

        array.insert(index, scalarValueToJsonNode(value));

        return document;
    }

}