package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/elt/">ELT</a>
 */
public class Elt implements FunctionDefinition {

    @Override
    public String getName() {
        return "ELT";
    }

    @Override
    public String getMethodName() {
        return "elt";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String elt(int n, String ...values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Incorrect parameter count");
        }

        if (n < 1 || n > values.length) {
            return null;
        }

        return values[n - 1];
    }
}
