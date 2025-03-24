package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/mid/">MID</a>
 */
public class Mid implements FunctionDefinition {

    @Override
    public String getName() {
        return "MID";
    }

    @Override
    public String getMethodName() {
        return "mid";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String mid(String str, Integer pos, Integer len) {
        if (str == null || pos == null || len == null) {
            return null;
        }

        if (pos == 0) {
            return "";
        }

        var length = str.length();
        var start = pos > 0 ? pos - 1 : length + pos;
        var end = Math.min(start + len, length);

        if (start < 0 || end > length) {
            return "";
        }

        return str.substring(start, end);
    }
}
