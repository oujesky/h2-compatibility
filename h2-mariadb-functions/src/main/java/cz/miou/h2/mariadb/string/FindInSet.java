package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

import java.util.List;
import java.util.regex.Pattern;

/**
 * <a href="https://mariadb.com/kb/en/find_in_set/">FIND_IN_SET</a>
 */
public class FindInSet implements FunctionDefinition {

    private static final Pattern SPLIT_PATTERN = Pattern.compile(",\\s*");
    
    @Override
    public String getName() {
        return "FIND_IN_SET";
    }

    @Override
    public String getMethodName() {
        return "findInSet";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static Integer findInSet(String pattern, String strList) {
        if (pattern == null || strList == null) {
            return null;
        }

        var values = List.of(SPLIT_PATTERN.split(strList));

        if (values.isEmpty()) {
            return 0;
        }

        var result = values.indexOf(pattern);

        if (result == -1) {
            return 0;
        }

        return result + 1;
    }
    
}