package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;

import static cz.miou.h2.mariadb.time.DateTimeUtil.parsePeriod;

/**
 * <a href="https://mariadb.com/kb/en/period_add/">PERIOD_ADD</a>
 */
public class PeriodAdd implements FunctionDefinition {

    @Override
    public String getName() {
        return "PERIOD_ADD";
    }

    @Override
    public String getMethodName() {
        return "periodAdd";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static int periodAdd(int period, int add) {
        if (period == 0) {
            return 0;
        }

        var yearMonth = parsePeriod(period, add);

        return yearMonth.getYear() * 100 + yearMonth.getMonthValue();
    }
}
