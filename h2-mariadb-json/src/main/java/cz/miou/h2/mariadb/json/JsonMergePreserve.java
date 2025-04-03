package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionAlias;

public class JsonMergePreserve implements FunctionAlias {

    @Override
    public String getName() {
        return "JSON_MERGE_PRESERVE";
    }

    @Override
    public String getClassName() {
        return "cz.miou.h2.mariadb.json.JsonMerge";
    }

    @Override
    public String getMethodName() {
        return "jsonMerge";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

}
