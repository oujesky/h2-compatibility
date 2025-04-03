package cz.miou.h2.mariadb.json;

import com.fasterxml.jackson.databind.JsonNode;
import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.util.ArrayDeque;

import static cz.miou.h2.mariadb.json.JsonUtil.jsonNodeFromValue;

/**
 * <a href="https://mariadb.com/kb/en/json_depth/">JSON_DEPTH</a>
 */
public class JsonDepth implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_DEPTH";
    }

    @Override
    public String getMethodName() {
        return "jsonDepth";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Integer jsonDepth(Value json) {
        if (json == null) {
            return null;
        }

        var root = jsonNodeFromValue(json);
        if (root == null) {
            return null;
        }

        var queue = new ArrayDeque<NodeDepth>();
        queue.add(new NodeDepth(root, 1));

        var result = 0;

        while (!queue.isEmpty()) {
            var nodeDepth = queue.poll();

            var depth = nodeDepth.depth;
            var node = nodeDepth.node;

            result = Math.max(result, nodeDepth.depth);
            node.iterator()
                .forEachRemaining(child -> queue.add(new NodeDepth(child, depth + 1)));
        }

        return result;
    }

    private static class NodeDepth {
        private final JsonNode node;
        private final int depth;

        private NodeDepth(JsonNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }
    
}