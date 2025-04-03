package cz.miou.h2.mariadb.json;

import cz.miou.h2.api.FunctionAlias;

public class JsonPretty implements FunctionAlias {

    @Override
    public String getName() {
        return "JSON_PRETTY";
    }

    @Override
    public String getClassName() {
        return "cz.miou.h2.mariadb.json.JsonDetailed";
    }

    @Override
    public String getMethodName() {
        return "jsonDetailed";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
}
