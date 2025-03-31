package cz.miou.h2.mariadb.binary;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/bit_count/">BIT_COUNT</a>
 */
public class BitCount implements FunctionDefinition {

    @Override
    public String getName() {
        return "BIT_COUNT";
    }

    @Override
    public String getMethodName() {
        return "bitCount";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Integer bitCount(Long value) {
        if (value == null) {
            return null;
        }

        return Long.bitCount(value);
    }
}
