package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;

import java.util.List;

/**
 * <a href="https://mariadb.com/kb/en/format_pico_time/">FORMAT_PICO_TIME</a>
 */
public class FormatPicoTime implements FunctionDefinition {

    private static final List<Long> THRESHOLDS = List.of(
        86400000000000000L, 3600000000000000L, 60000000000000L,
        1000000000000L, 1000000000L, 1000000L, 1000L
    );

    private static final List<String> UNITS = List.of("d", "h", "min", "s", "ms", "us", "ns");

    @Override
    public String getName() {
        return "FORMAT_PICO_TIME";
    }

    @Override
    public String getMethodName() {
        return "formatPicoTime";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String formatPicoTime(long timeVal) {
        var size = THRESHOLDS.size();
        for (int i = 0; i < size; i++) {
            var threshold = THRESHOLDS.get(i);
            var unit = UNITS.get(i);

            if (timeVal >= threshold) {
                return String.format("%.2f %s", (double) timeVal / threshold, unit);
            }
        }

        return String.format("%d ps", timeVal);
    }
}
