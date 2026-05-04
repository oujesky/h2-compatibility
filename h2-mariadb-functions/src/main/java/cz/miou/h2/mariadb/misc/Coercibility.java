package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

/**
 * <a href="https://mariadb.com/kb/en/coercibility/">COERCIBILITY</a>
 */
public class Coercibility implements FunctionDefinition {

    private enum CoercibilityValue {
        COERCIBLE(4),
        NUMERIC(5),
        IGNORABLE(6);

        private final int value;

        CoercibilityValue(int value) {
            this.value = value;
        }
    }

    @Override
    public String getName() {
        return "COERCIBILITY";
    }

    @Override
    public String getMethodName() {
        return "coercibility";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Integer coercibility(Value value) {
        if (value.containsNull()) {
            return CoercibilityValue.IGNORABLE.value;
        }

        switch (value.getValueType()) {
            case Value.CHAR:
            case Value.VARCHAR:
            case Value.CLOB:
            case Value.VARCHAR_IGNORECASE:
            case Value.BINARY:
            case Value.VARBINARY:
            case Value.BLOB:
                return CoercibilityValue.COERCIBLE.value;
            default:
                return CoercibilityValue.NUMERIC.value;
        }
    }
}
