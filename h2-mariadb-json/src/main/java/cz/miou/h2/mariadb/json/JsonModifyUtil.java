package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import cz.miou.h2.mariadb.json.JsonUtil.ArrayPathWithParent;
import cz.miou.h2.mariadb.json.JsonUtil.ObjectPathWithParent;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import java.util.List;
import java.util.function.Predicate;

import static cz.miou.h2.mariadb.json.JsonUtil.createJsonArrayDocument;
import static cz.miou.h2.mariadb.json.JsonUtil.createJsonDocument;
import static cz.miou.h2.mariadb.json.JsonUtil.getPathWithParent;
import static cz.miou.h2.mariadb.json.JsonUtil.isRootPath;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonDocumentFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.jsonPathExists;
import static cz.miou.h2.mariadb.json.JsonUtil.scalarValueToJsonNode;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

class JsonModifyUtil {

    public static Value jsonModify(Value json, Value[] values, Modification modification) {
        if (json == null || values == null || values.length == 0 || values.length % 2 != 0) {
            return ValueNull.INSTANCE;
        }

        var document = jsonDocumentFromValue(json);
        if (document == null) {
            return ValueNull.INSTANCE;
        }

        for (int i = 0; i < values.length; i += 2) {
            if (values[i] == null) {
                return ValueNull.INSTANCE;
            }

            var path = JsonPath.compile(values[i].getString());
            if (!path.isDefinite()) {
                return ValueNull.INSTANCE;
            }

            var result = modification.modify(document, path, values[i + 1]);

            if (result == null) {
                return ValueNull.INSTANCE;
            } else {
                document = result;
            }

        }

        return toJsonValue(document);
    }

    public static Value jsonModify(Value json, Value[] values, Predicate<JsonNode> predicate, boolean replace) {
        return jsonModify(
            json,
            values,
            (document, path, value) -> modifyDocument(document, path, scalarValueToJsonNode(value), predicate, replace)
        );
    }

    @FunctionalInterface
    public interface Modification {
        DocumentContext modify(DocumentContext document, JsonPath path, Value value);
    }

    public static DocumentContext modifyDocument(DocumentContext document, JsonPath path, JsonNode value, Predicate<JsonNode> predicate, boolean replace) {
        var node = document.read(path, JsonNode.class);

        if (!predicate.test(node)) {
            return document;
        }

        var pathWithParent = getPathWithParent(path);
        if (pathWithParent == null) {
            return null;
        }

        if (pathWithParent instanceof ObjectPathWithParent && (!replace || (node != null && !node.isMissingNode()))) {
            return modifyObjectNode(document, (ObjectPathWithParent) pathWithParent, value);
        }

        if (pathWithParent instanceof ArrayPathWithParent) {
            return modifyArrayNode(document, (ArrayPathWithParent) pathWithParent, value, replace);
        }

        return document;
    }

    private static DocumentContext modifyObjectNode(DocumentContext document, ObjectPathWithParent path, JsonNode value) {
        if (jsonPathExists(document, path.getParentPath())) {
            document.put(path.getParentPath(), path.getKey(), value);
        }

        return document;
    }

    private static DocumentContext modifyArrayNode(DocumentContext document, ArrayPathWithParent path, JsonNode value, boolean replace) {
        var parentPath = path.getParentPath();
        var parentNode = document.read(parentPath, JsonNode.class);
        if (parentNode != null) {
            var index = path.getIndex();

            document = replace
                ? replaceArrayNode(document, value, parentNode, index, parentPath)
                : addArrayNode(document, value, parentNode, index, parentPath);
        }
        return document;
    }

    private static DocumentContext replaceArrayNode(DocumentContext document, JsonNode value, JsonNode parentNode, int index, JsonPath parentPath) {
        if (parentNode instanceof ArrayNode) {
            if (index < 0 || index >= parentNode.size()) {
                return document;
            }

            var arrayNode = (ArrayNode) parentNode;
            arrayNode.set(index, value);
        } else if (index == 0) {
            if (isRootPath(parentPath)) {
                document = createJsonDocument(value);
            } else {
                document.set(parentPath, value);
            }
        }
        return document;
    }

    private static DocumentContext addArrayNode(DocumentContext document, JsonNode value, JsonNode parentNode, int index, JsonPath parentPath) {
        if (parentNode instanceof ArrayNode && index >= parentNode.size() - 1) {
            var arrayNode = (ArrayNode) parentNode;
            arrayNode.add(value);
        } else if (index > 0) {
            if (isRootPath(parentPath)) {
                document = createJsonArrayDocument();
                document.add(parentPath, parentNode);
                document.add(parentPath, value);
            } else {
                document.set(parentPath, List.of(parentNode, value));
            }
        }
        return document;
    }
}
