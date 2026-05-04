package cz.miou.h2.test;

import cz.miou.h2.api.ExtensionLoader;

public class TestExtensionLoader implements ExtensionLoader {

    private static boolean loaded = false;

    @Override
    public String getName() {
        return "TEST_EXTENSION";
    }

    @Override
    public String getClassName() {
        return "cz.miou.h2.test.TestExtensionLoader";
    }

    @Override
    public String getMethodName() {
        return "testLoader";
    }

    @SuppressWarnings("unused")
    public static void testLoader() {
        loaded = true;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static void reset() {
        loaded = false;
    }
}
