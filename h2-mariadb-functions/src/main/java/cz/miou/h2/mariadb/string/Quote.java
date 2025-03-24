package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/quote/">QUOTE</a>
 */
public class Quote implements FunctionDefinition {

    @Override
    public String getName() {
        return "QUOTE";
    }

    @Override
    public String getMethodName() {
        return "quote";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    public static String quote(String input) {
        if (input == null) {
            return "NULL";
        }

        var replaced = input
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\0", "\\0")
            .replace("\26", "\\26");

        return String.format("'%s'", replaced);
    }
}
