package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.readJsonNode;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

class JsonMergeUtil {

    private JsonMergeUtil() {}

    public static Value mergeJson(Value json, String[] patches, boolean preserve) {
        if (json == null || patches == null || patches.length == 0) {
            return ValueNull.INSTANCE;
        }

        var result = jsonNodeFromValue(json);
        if (result == null) {
            return ValueNull.INSTANCE;
        }

        for (var patch : patches) {
            var node = readJsonNode(patch);
            if (node == null) {
                return ValueNull.INSTANCE;
            }

            result = mergeNode(result, node, preserve);
        }

        return toJsonValue(result);
    }

    private static JsonNode mergeNode(JsonNode source, JsonNode target, boolean preserve) {
        if (preserve && (source.isArray() || target.isArray())) {
            return mergeArray(source, target);
        }

        if (source.isObject() && target.isObject()) {
            return mergeObject((ObjectNode) source, (ObjectNode) target, preserve);
        }

        return preserve ? wrapArray(source, target) : target;
    }

    private static ObjectNode mergeObject(ObjectNode source, ObjectNode target, boolean preserve) {
        target.fieldNames().forEachRemaining(name -> {
            if (source.has(name) && target.has(name)) {
                if (!preserve && target.get(name).isNull()) {
                    source.remove(name);
                } else {
                    source.set(name, mergeNode(source.get(name), target.get(name), preserve));
                }
            } else if (target.has(name)) {
                source.set(name, target.get(name));
            }
        });

        return source;
    }

    private static ArrayNode mergeArray(JsonNode source, JsonNode target) {
        var array = ensureArray(source);
        array.addAll(ensureArray(target));
        return array;
    }

    private static ArrayNode ensureArray(JsonNode node) {
        if (node.isArray()) {
            return (ArrayNode) node;
        } else {
            var array = JsonNodeFactory.instance.arrayNode();
            array.add(node);
            return array;
        }
    }

    private static ArrayNode wrapArray(JsonNode source, JsonNode target) {
        return JsonNodeFactory.instance.arrayNode()
            .add(source)
            .add(target);
    }
}
