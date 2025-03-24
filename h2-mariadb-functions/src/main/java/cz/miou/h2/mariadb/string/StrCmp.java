package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/strcmp/">STRCMP</a>
 */
public class StrCmp implements FunctionDefinition {

    @Override
    public String getName() {
        return "STRCMP";
    }

    @Override
    public String getMethodName() {
        return "strCmp";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Integer strCmp(String a, String b) {
        if (a == null || b == null) {
            return null;
        }

        return a.compareTo(b);
    }
}
