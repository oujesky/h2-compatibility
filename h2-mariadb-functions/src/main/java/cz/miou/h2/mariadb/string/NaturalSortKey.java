package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/kb/en/natural_sort_key/">NATURAL_SORT_KEY</a>
 */
public class NaturalSortKey implements FunctionDefinition {

    @Override
    public String getName() {
        return "NATURAL_SORT_KEY";
    }

    @Override
    public String getMethodName() {
        return "naturalSortKey";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String naturalSortKey(String in) {
        if (in == null) {
            return null;
        }

        return toNatsortKey(in);
    }

    private static String toNatsortKey(String in) {
        // adapted from https://github.com/MariaDB/server/blob/main/sql/item_strfunc.cc#L5934

        var length = in.length();
        var out = new StringBuilder();

        int nDigits = 0;
        int nLeadZeros = 0;
        int numStart = 0;

        for (var pos = 0; ; pos++) {
            char c = pos < length ? in.charAt(pos) : 0;
            var isDigit = Character.isDigit(c);
            if (!isDigit && (nDigits > 0 || nLeadZeros > 0)) {
                /* Handle end of digits run.*/
                if (nDigits == 0) {
                    /*We only have zeros.*/
                    numStart = pos - 1;
                    nDigits = 1;
                }
                natsortEncodeNumericString(in.substring(numStart), nDigits, out);

                /* Reset state.*/
                nDigits = 0;
                numStart = -1;
                nLeadZeros = 0;
            }

            if (pos == length) {
                break;
            }

            if (!isDigit) {
                out.append(c);
            } else if (c == '0' && nDigits == 0) {
                nLeadZeros++;
            } else if (nDigits++ == 0) {
                numStart = pos;
            }
        }

        return out.toString();
    }

    private static void natsortEncodeNumericString(String in, int nDigits, StringBuilder out) {
        natsortEncodeLength(nDigits - 1, out);
        out.append(in, 0, nDigits);
    }

    private static void natsortEncodeLength(int n, StringBuilder out) {
        if (n < 27) {
            if (n >= 9) {
                out.append("9".repeat(out.length() + n / 9));
            }
            out.appendCodePoint((char) (n % 9 + '0'));
            return;
        }

        var log10n = 0;
        for (var tmp = n / 10; tmp > 0; tmp /= 10) {
            log10n++;
        }
        out.append("9".repeat(out.length() + 3));
        out.appendCodePoint('0' + (char) (log10n / 10));
        out.appendCodePoint('0' + (char) (log10n % 10));
        out.append(n);
    }
}
