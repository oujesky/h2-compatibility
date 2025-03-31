package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;

import static cz.miou.h2.mariadb.time.DateTimeUtil.valueToTemporal;

/**
 * <a href="https://mariadb.com/kb/en/yearweek/">YEARWEEK</a>
 */
public class YearWeek implements FunctionDefinition {

    @Override
    public String getName() {
        return "YEARWEEK";
    }

    @Override
    public String getMethodName() {
        return "yearWeek";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static int yearWeek(Connection connection, Value value) {
        var temporal = valueToTemporal(connection, value);

        var weekFields = WeekFields.of(DayOfWeek.SUNDAY, 7);
        var year = temporal.get(weekFields.weekBasedYear());
        var week = temporal.get(weekFields.weekOfWeekBasedYear());

        return year * 100 + week;
    }
}
