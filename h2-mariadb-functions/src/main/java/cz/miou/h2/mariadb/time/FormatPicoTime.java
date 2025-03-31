package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;

import java.util.List;

import static cz.miou.h2.mariadb.util.ConversionUtil.formatWithUnit;

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
        return formatWithUnit(timeVal, THRESHOLDS, UNITS, "ps");
    }
}
