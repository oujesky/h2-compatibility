package cz.miou.h2.mariadb.aggregate;

import cz.miou.h2.api.AggregateDefinition;
import org.h2.api.AggregateFunction;

import java.math.BigDecimal;
import java.sql.Types;

/**
 * <a href="https://mariadb.com/kb/en/bit_xor/">BIT_XOR</a>
 */
public class BitXor implements AggregateDefinition, AggregateFunction {

    private long result;

    @Override
    public String getName() {
        return "BIT_XOR";
    }

    @Override
    public int getType(int[] inputTypes) {
        return Types.NUMERIC;
    }

    @Override
    public void add(Object value) {
        if (value instanceof Number) {
            result ^= ((Number) value).longValue();
        }
    }

    @Override
    public Object getResult() {
        return new BigDecimal(Long.toUnsignedString(result));
    }
}
