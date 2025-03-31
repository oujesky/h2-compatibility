package cz.miou.h2.mariadb.util;

import org.h2.value.Value;
import org.h2.value.ValueChar;
import org.h2.value.ValueDouble;
import org.h2.value.ValueNull;
import org.h2.value.ValueNumeric;
import org.h2.value.ValueVarchar;

import java.math.RoundingMode;
import java.util.List;

public class ConversionUtil {

    private ConversionUtil() {}

    public static String convert(Value input, Integer fromBase, Integer toBase, boolean signed) {
        if (input instanceof ValueNull) {
            return null;
        }

        var value = prepareValue(input, fromBase);

        var converted = signed
            ? Long.toString(value, toBase)
            : Long.toUnsignedString(value, toBase);

        return converted.toUpperCase();
    }

    public static String convert(Value input, int fromBase, int toBase) {
        return convert(input, fromBase, toBase, false);
    }

    private static long prepareValue(Value input, int base) {
        if (input instanceof ValueVarchar || input instanceof ValueChar) {
            return Long.parseLong(input.getString(), base);
        }

        if (input instanceof ValueDouble || input instanceof ValueNumeric) {
            return input.getBigDecimal()
                .setScale(0, RoundingMode.FLOOR)
                .longValue();
        }

        return input.getLong();
    }

    public static String formatWithUnit(long value, List<Long> thresholds, List<String> units, String defaultUnit) {
        var size = thresholds.size();
        for (int i = 0; i < size; i++) {
            var threshold = thresholds.get(i);
            var unit = units.get(i);

            if (value >= threshold) {
                return String.format("%.2f %s", (double) value / threshold, unit);
            }
        }

        return String.format("%d %s", value, defaultUnit);
    }

}
