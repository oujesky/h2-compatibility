package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.ValueDate;

import static cz.miou.h2.mariadb.time.DateTimeUtil.EPOCH_START_IN_DAYS;
import static org.h2.util.DateTimeUtils.dateValueFromAbsoluteDay;

/**
 * <a href="https://mariadb.com/kb/en/from_days/">FROM_DAYS</a>
 */
public class FromDays implements FunctionDefinition {

    @Override
    public String getName() {
        return "FROM_DAYS";
    }

    @Override
    public String getMethodName() {
        return "fromDays";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static ValueDate fromDays(long days) {
        if (days < 366) {
            return ValueDate.fromDateValue(0);
        }

        return ValueDate.fromDateValue(dateValueFromAbsoluteDay(days - EPOCH_START_IN_DAYS));
    }
}
