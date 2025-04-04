package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import static cz.miou.h2.mariadb.util.LocaleUtil.findLocale;

/**
 * <a href="https://mariadb.com/kb/en/format/">FORMAT</a>
 * Differences to MariaDB:
 * <ul>
 *   <li>Locale-based number formats are coming from Java implementation, that might have slight differences to MariaDB
 * </ul>
 */
public class Format implements FunctionDefinition {

    @Override
    public String getName() {
        return "FORMAT";
    }

    @Override
    public String getMethodName() {
        return "format";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    public static String format(BigDecimal number, Integer decimalPosition) {
        return format(number, decimalPosition, null);
    }

    public static String format(BigDecimal number, Integer decimalPosition, String language) {
        if (number == null || decimalPosition == null || decimalPosition < 0) {
            return null;
        }

        var locale = findLocale(language);

        NumberFormat format = decimalPosition == 0
            ? NumberFormat.getIntegerInstance(locale)
            : getNumberFormat(decimalPosition, locale);

        return format.format(number);
    }

    private static NumberFormat getNumberFormat(Integer decimalPosition, Locale locale) {
        var format = NumberFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(decimalPosition);
        format.setMaximumFractionDigits(decimalPosition);
        format.setRoundingMode(RoundingMode.HALF_DOWN);
        return format;
    }
}
