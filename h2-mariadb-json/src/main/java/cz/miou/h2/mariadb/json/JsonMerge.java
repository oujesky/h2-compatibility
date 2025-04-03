package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.json.JsonMergeUtil.mergeJson;

/**
 * <a href="https://mariadb.com/kb/en/json_merge/">JSON_MERGE</a>
 */
public class JsonMerge implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "JSON_MERGE";
    }

    @Override
    public String getMethodName() {
        return "jsonMerge";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Value jsonMerge(Value json, String... patches) {
        return mergeJson(json, patches, true);
    }

}