package cz.miou.h2.mariadb.string;

import org.h2.value.Value;
import org.h2.value.ValueDouble;
import org.h2.value.ValueNull;
import org.h2.value.ValueNumeric;
import org.h2.value.ValueVarchar;

import java.math.RoundingMode;

class ConversionUtil {

    private ConversionUtil() {}

    public static String convert(Value input, int baseFrom, int baseTo) {
        if (input instanceof ValueNull) {
            return null;
        }

        var value = prepareValue(input, baseFrom);
        return Long.toUnsignedString(value, baseTo).toUpperCase();
    }

    private static long prepareValue(Value input, int base) {
        if (input instanceof ValueVarchar) {
            return Long.parseLong(input.getString(), base);
        }

        if (input instanceof ValueDouble || input instanceof ValueNumeric) {
            return input.getBigDecimal()
                .setScale(0, RoundingMode.FLOOR)
                .longValue();
        }

        return input.getLong();
    }
}
