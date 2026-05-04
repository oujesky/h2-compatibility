package cz.miou.h2.mariadb.numeric;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;


/**
 * <a href="https://mariadb.com/docs/server/reference/sql-functions/numeric-functions/to_number">TO_NUMBER</a>
 */
public class ToNumber implements FunctionDefinition {

    @Override
    public String getName() {
        return "TO_NUMBER";
    }

    @Override
    public String getMethodName() {
        return "toNumber";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Double toNumber(Value value) {
        if (value.containsNull()) {
            return null;
        }

        try {
            var result = value.getDouble();
            return Double.isFinite(result) ? result : null;
        } catch (RuntimeException e) {
            return null;
        }
    }

    @SuppressWarnings("unused")
    public static Double toNumber(Value value, String format) {
        if (value.containsNull() || format == null) {
            return null;
        }

        if (!isCharacterValue(value)) {
            throw new IllegalArgumentException("TO_NUMBER with a format requires a string input");
        }

        return FormatModel.parse(format).parseInput(value.getString());
    }

    private static boolean isCharacterValue(Value value) {
        switch (value.getValueType()) {
            case Value.CHAR:
            case Value.VARCHAR:
            case Value.CLOB:
            case Value.VARCHAR_IGNORECASE:
                return true;
            default:
                return false;
        }
    }

    private static final class FormatModel {

        private final boolean leadingSign;
        private final boolean trailingSign;
        private final boolean trailingMinus;
        private final boolean angleBrackets;
        private final boolean currency;
        private final boolean scientific;
        private final boolean hexadecimal;
        private final int minimumIntegerDigits;
        private final int maximumIntegerDigits;
        private final int maximumFractionDigits;

        private FormatModel(
            String numberFormat,
            boolean leadingSign,
            boolean trailingSign,
            boolean trailingMinus,
            boolean angleBrackets,
            boolean currency,
            boolean scientific,
            boolean hexadecimal
        ) {
            this.leadingSign = leadingSign;
            this.trailingSign = trailingSign;
            this.trailingMinus = trailingMinus;
            this.angleBrackets = angleBrackets;
            this.currency = currency;
            this.scientific = scientific;
            this.hexadecimal = hexadecimal;

            var decimalIndex = numberFormat.indexOf('.');
            var integerFormat = decimalIndex >= 0 ? numberFormat.substring(0, decimalIndex) : numberFormat;
            var fractionFormat = decimalIndex >= 0 ? numberFormat.substring(decimalIndex + 1) : "";

            this.minimumIntegerDigits = count(integerFormat, '0');
            this.maximumIntegerDigits = countDigits(integerFormat);
            this.maximumFractionDigits = countDigits(fractionFormat);
        }

        static FormatModel parse(String format) {
            var normalized = format.toUpperCase().replace("FM", "");

            rejectUnsupported(normalized);

            var leadingSign = normalized.startsWith("S");
            if (leadingSign) {
                normalized = normalized.substring(1);
            }

            var angleBrackets = normalized.endsWith("PR");
            if (angleBrackets) {
                normalized = normalized.substring(0, normalized.length() - 2);
            }

            var trailingMinus = normalized.endsWith("MI");
            if (trailingMinus) {
                normalized = normalized.substring(0, normalized.length() - 2);
            }

            var trailingSign = normalized.endsWith("S");
            if (trailingSign) {
                normalized = normalized.substring(0, normalized.length() - 1);
            }

            var scientific = normalized.endsWith("EEEE");
            if (scientific) {
                normalized = normalized.substring(0, normalized.length() - 4);
            }

            var hexadecimal = normalized.indexOf('X') >= 0;
            var currency = normalized.indexOf('$') >= 0;

            validateSupportedSyntax(normalized, hexadecimal);

            var numberFormat = normalized
                .replace("$", "")
                .replace("B", "")
                .replace(",", "");

            return new FormatModel(
                numberFormat,
                leadingSign,
                trailingSign,
                trailingMinus,
                angleBrackets,
                currency,
                scientific,
                hexadecimal
            );
        }

        Double parseInput(String input) {
            var subject = stripAffixes(input);
            if (subject == null) {
                return null;
            }

            if (hexadecimal) {
                return parseHexadecimal(subject);
            }

            if (scientific) {
                return parseScientific(subject);
            }

            return parseFixed(subject);
        }

        private String stripAffixes(String input) {
            var subject = input.trim();
            var negative = false;

            if (angleBrackets) {
                if (subject.startsWith("<") && subject.endsWith(">")) {
                    negative = true;
                    subject = subject.substring(1, subject.length() - 1);
                } else if (subject.indexOf('<') >= 0 || subject.indexOf('>') >= 0) {
                    return null;
                }
            }

            if (leadingSign) {
                if (subject.startsWith("-")) {
                    negative = true;
                    subject = subject.substring(1);
                } else if (subject.startsWith("+")) {
                    subject = subject.substring(1);
                } else {
                    return null;
                }
            }

            if (trailingSign) {
                if (subject.endsWith("-")) {
                    negative = true;
                    subject = subject.substring(0, subject.length() - 1);
                } else if (subject.endsWith("+")) {
                    subject = subject.substring(0, subject.length() - 1);
                } else {
                    return null;
                }
            }

            if (trailingMinus && subject.endsWith("-")) {
                negative = true;
                subject = subject.substring(0, subject.length() - 1);
            }

            if (currency) {
                if (subject.indexOf('$') < 0) {
                    return null;
                }
                subject = subject.replace("$", "");
            }

            subject = subject.trim().replace(" ", "");
            if (subject.isEmpty()) {
                return null;
            }
            return negative ? "-" + subject : subject;
        }

        private Double parseHexadecimal(String subject) {
            var normalized = subject;
            var negative = normalized.startsWith("-");
            if (negative) {
                normalized = normalized.substring(1);
            }

            if (normalized.isEmpty() || !normalized.matches("[0-9A-Fa-f]+")) {
                return null;
            }

            if (normalized.length() > maximumIntegerDigits) {
                return null;
            }

            try {
                var result = Long.parseLong(normalized, 16);
                return (double) (negative ? -result : result);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        private Double parseScientific(String subject) {
            var normalized = subject.replace(",", "");
            try {
                var result = Double.parseDouble(normalized);
                return Double.isFinite(result) ? result : null;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        private Double parseFixed(String subject) {
            var normalized = subject.replace(",", "");
            if (normalized.indexOf('.') != normalized.lastIndexOf('.')) {
                return null;
            }

            var negative = normalized.startsWith("-");
            if (negative) {
                normalized = normalized.substring(1);
            }

            var decimalIndex = normalized.indexOf('.');
            var integer = decimalIndex >= 0 ? normalized.substring(0, decimalIndex) : normalized;
            var fraction = decimalIndex >= 0 ? normalized.substring(decimalIndex + 1) : "";

            if (!integer.matches("\\d*") || !fraction.matches("\\d*")) {
                return null;
            }

            if (integer.length() < minimumIntegerDigits || integer.length() > maximumIntegerDigits) {
                return null;
            }

            if (fraction.length() > maximumFractionDigits) {
                return null;
            }

            if (integer.isEmpty() && fraction.isEmpty()) {
                return 0D;
            }

            var text = (integer.isEmpty() ? "0" : integer) + (fraction.isEmpty() ? "" : "." + fraction);
            try {
                var result = Double.parseDouble(text);
                return negative ? -result : result;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        private static void rejectUnsupported(String format) {
            var unsupported = new String[] {"TM", "RN", "G", "D", "C", "L", "U", "V"};
            for (var token : unsupported) {
                if (format.contains(token)) {
                    throw new IllegalArgumentException("Unsupported TO_NUMBER format token: " + token);
                }
            }
        }

        private static void validateSupportedSyntax(String format, boolean hexadecimal) {
            var allowed = hexadecimal ? "0X" : "09.,$B";
            for (var i = 0; i < format.length(); i++) {
                if (allowed.indexOf(format.charAt(i)) < 0) {
                    throw new IllegalArgumentException("Unsupported TO_NUMBER format token: " + format.charAt(i));
                }
            }
        }

        private static int countDigits(String value) {
            return count(value, '0') + count(value, '9') + count(value, 'X');
        }

        private static int count(String value, char needle) {
            var result = 0;
            for (var i = 0; i < value.length(); i++) {
                if (value.charAt(i) == needle) {
                    result++;
                }
            }
            return result;
        }
    }
}
