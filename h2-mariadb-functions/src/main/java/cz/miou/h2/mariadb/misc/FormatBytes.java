package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;

import java.util.List;

import static cz.miou.h2.mariadb.util.ConversionUtil.formatWithUnit;

/**
 * <a href="https://mariadb.com/kb/en/miscellaneous-functions-format_bytes/">FORMAT_BYTES</a>
 */
public class FormatBytes implements FunctionDefinition {

    private static final List<Long> THRESHOLDS = List.of(
        1125899906842624L, 1099511627776L, 1073741874L, 1048576L, 1024L
    );

    private static final List<String> UNITS = List.of("PiB", "TiB", "GiB", "MiB", "KiB");
    
    @Override
    public String getName() {
        return "FORMAT_BYTES";
    }

    @Override
    public String getMethodName() {
        return "formatBytes";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
    
    @SuppressWarnings("unused")
    public static String formatBytes(Long value) {
        if (value == null) {
            return null;
        }

        return formatWithUnit(value, THRESHOLDS, UNITS, "bytes");
    }
}