package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;
import java.time.LocalTime;

import static cz.miou.h2.mariadb.time.DateTimeUtil.valueToTemporal;

/**
 * <a href="https://mariadb.com/kb/en/time-function/">TIME</a>
 */
public class Time implements FunctionDefinition {

    @Override
    public String getName() {
        return "TIME";
    }

    @Override
    public String getMethodName() {
        return "time";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static LocalTime time(Connection connection, Value input) {
        return LocalTime.from(valueToTemporal(connection, input));
    }
}
