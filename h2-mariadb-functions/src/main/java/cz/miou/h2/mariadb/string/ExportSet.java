package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/export_set/">EXPORT_SET</a>
 */
public class ExportSet implements FunctionDefinition {
    
    @Override
    public String getName() {
        return "EXPORT_SET";
    }

    @Override
    public String getMethodName() {
        return "exportSet";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String exportSet(Long bits, String on, String off) {
        return exportSet(bits, on, off, ",");
    }

    @SuppressWarnings("unused")
    public static String exportSet(Long bits, String on, String off, String separator) {
        return exportSet(bits, on, off, separator, 64);
    }

    @SuppressWarnings("unused")
    public static String exportSet(Long bits, String on, String off, String separator, Integer numberOfBits) {
        if (bits == null || on == null || off == null || separator == null || numberOfBits == null) {
            return null;
        }

        if (numberOfBits > 64 || numberOfBits < 0) {
            numberOfBits = 64;
        }

        var sb = new StringBuilder();

        for (var i = 0; i < numberOfBits; i++) {
            if (i > 0) {
                sb.append(separator);
            }

            var x = bits >> i;
            sb.append((x & 1) > 0 ? on : off);
        }

        return sb.toString();
    }

}