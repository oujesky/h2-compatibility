package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;

import java.util.LinkedHashSet;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;
import static cz.miou.h2.mariadb.json.JsonUtil.toJsonValue;

/**
 * <a href="https://mariadb.com/kb/en/json_array_intersect/">JSON_ARRAY_INTERSECT</a>
 */
public class JsonArrayIntersect implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_ARRAY_INTERSECT";
    }

    @Override
    public String getMethodName() {
        return "jsonArrayIntersect";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonArrayIntersect(Value arr1, Value arr2) {
        if (arr1 == null || arr2 == null) {
            return ValueNull.INSTANCE;
        }

        var node1 = jsonNodeFromValue(arr1);
        if (node1 == null || !node1.isArray()) {
            return ValueNull.INSTANCE;
        }

        var node2 = jsonNodeFromValue(arr2);
        if (node2 == null || !node2.isArray()) {
            return ValueNull.INSTANCE;
        }

        var set1 = new LinkedHashSet<JsonNode>();
        node1.elements().forEachRemaining(set1::add);
        var set2 = new LinkedHashSet<JsonNode>();
        node2.elements().forEachRemaining(set2::add);

        set1.retainAll(set2);

        if (set1.isEmpty()) {
            return ValueNull.INSTANCE;
        }

        return toJsonValue(set1);
    }

}