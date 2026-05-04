package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

/**
 * <a href="https://mariadb.com/kb/en/collation/">COLLATION</a>
 */
public class Collation implements FunctionDefinition {

    private static final String BINARY = "binary";
    private static final String UTF8MB4_GENERAL_CI = "utf8mb4_general_ci";

    @Override
    public String getName() {
        return "COLLATION";
    }

    @Override
    public String getMethodName() {
        return "collation";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String collation(Value value) {
        switch (value.getValueType()) {
            case Value.VARCHAR:
            case Value.VARCHAR_IGNORECASE:
            case Value.CHAR:
            case Value.CLOB:
                return UTF8MB4_GENERAL_CI;
            default:
                return BINARY;
        }
    }
}
