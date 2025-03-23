package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;
import java.time.temporal.ChronoField;

import static cz.miou.h2.mariadb.time.DateTimeUtil.valueToTemporal;

/**
 * <a href="https://mariadb.com/kb/en/weekday/">WEEKDAY</a>
 */
public class WeekDay implements FunctionDefinition {

    @Override
    public String getName() {
        return "WEEKDAY";
    }

    @Override
    public String getMethodName() {
        return "weekDay";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static int weekDay(Connection connection, Value value) {
        var temporal = valueToTemporal(connection, value);
        return temporal.get(ChronoField.DAY_OF_WEEK) - 1;
    }
}
