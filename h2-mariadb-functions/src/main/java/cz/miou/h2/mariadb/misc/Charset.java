package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

/**
 * <a href="https://mariadb.com/kb/en/charset/">CHARSET</a>
 */
public class Charset implements FunctionDefinition {

    private static final String BINARY = "binary";
    private static final String UTF8MB4 = "utf8mb4";

    @Override
    public String getName() {
        return "CHARSET";
    }

    @Override
    public String getMethodName() {
        return "charset";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String charset(Value value) {
        switch (value.getValueType()) {
            case Value.VARCHAR:
            case Value.VARCHAR_IGNORECASE:
            case Value.CHAR:
            case Value.CLOB:
                return UTF8MB4;
            default:
                return BINARY;
        }
    }
}
