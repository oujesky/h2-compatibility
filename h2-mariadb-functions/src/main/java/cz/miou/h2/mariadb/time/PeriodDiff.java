package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;

import java.time.temporal.ChronoField;

import static cz.miou.h2.mariadb.time.DateTimeUtil.parsePeriod;

/**
 * <a href="https://mariadb.com/kb/en/period_add/">PERIOD_DIFF</a>
 */
public class PeriodDiff implements FunctionDefinition {

    @Override
    public String getName() {
        return "PERIOD_DIFF";
    }

    @Override
    public String getMethodName() {
        return "periodDiff";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static int periodDiff(int period1, int period2) {
        if (period1 == 0 || period2 == 0) {
            return 0;
        }

        var months1 = parsePeriod(period1).getLong(ChronoField.PROLEPTIC_MONTH);
        var months2 = parsePeriod(period2).getLong(ChronoField.PROLEPTIC_MONTH);

        return (int) (months1 - months2);
    }
}
