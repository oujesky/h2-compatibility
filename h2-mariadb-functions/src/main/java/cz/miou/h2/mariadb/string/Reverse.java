package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/reverse/">REVERSE</a>
 */
public class Reverse implements FunctionDefinition {

    @Override
    public String getName() {
        return "REVERSE";
    }

    @Override
    public String getMethodName() {
        return "reverse";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String reverse(String input) {
        if (input == null) {
            return null;
        }
        return new StringBuilder(input).reverse().toString();
    }
}
