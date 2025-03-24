package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/substring_index/">SUBSTRING_INDEX</a>
 */
public class SubstringIndex implements FunctionDefinition {

    @Override
    public String getName() {
        return "SUBSTRING_INDEX";
    }

    @Override
    public String getMethodName() {
        return "substringIndex";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String substringIndex(String str, String delim, Integer count) {
        if (str == null || delim == null || count == null) {
            return null;
        }

        if (count == 0) {
            return "";
        }

        if (count > 0) {
            var index = -1;
            var found = 0;
            while ((index = str.indexOf(delim, index + 1)) != -1) {
                found++;
                if (found == count) {
                    return str.substring(0, index);
                }
            }
        } else {
            var index = str.length();
            var found = 0;
            while ((index = str.lastIndexOf(delim, index - 1)) != -1) {
                found++;
                if (found == -count) {
                    return str.substring(index + delim.length());
                }
            }
        }

        return str;
    }
}
