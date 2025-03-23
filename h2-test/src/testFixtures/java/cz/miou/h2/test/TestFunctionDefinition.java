package cz.miou.h2.test;

import cz.miou.h2.api.FunctionDefinition;

public class TestFunctionDefinition implements FunctionDefinition {

    @Override
    public String getName() {
        return "TEST_FUNCTION";
    }

    @Override
    public String getMethodName() {
        return "test";
    }

    @SuppressWarnings("unused")
    public static String test(String input) {
        return "TEST:" + input;
    }

    @SuppressWarnings("unused")
    public static String test(String input, int number) {
        return "TEST:" + input + ":" + number;
    }
}
