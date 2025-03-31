package cz.miou.h2.test;

import cz.miou.h2.api.FunctionAlias;

public class TestFunctionAlias implements FunctionAlias {

    @Override
    public String getName() {
        return "TEST_FUNCTION_ALIAS";
    }

    @Override
    public String getClassName() {
        return "cz.miou.h2.test.TestFunctionDefinition";
    }

    @Override
    public String getMethodName() {
        return "test";
    }


}
