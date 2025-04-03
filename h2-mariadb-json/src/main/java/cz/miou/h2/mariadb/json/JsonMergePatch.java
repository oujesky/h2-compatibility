package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonMergeUtil.mergeJson;

/**
 * <a href="https://mariadb.com/kb/en/json_merge_patch/">JSON_MERGE_PATCH</a>
 */
public class JsonMergePatch implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_MERGE_PATCH";
    }

    @Override
    public String getMethodName() {
        return "jsonMergePatch";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonMergePatch(Value json, String...patches) {
        return mergeJson(json, patches, false);
    }
    
}