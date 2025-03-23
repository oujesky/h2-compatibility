package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static cz.miou.h2.mariadb.time.DateTimeUtil.valueToTemporal;

/**
 * <a href="https://mariadb.com/kb/en/timestamp-function/">TIMESTAMP</a>
 */
public class Timestamp implements FunctionDefinition {

    @Override
    public String getName() {
        return "TIMESTAMP";
    }

    @Override
    public String getMethodName() {
        return "timestamp";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static LocalDateTime timestamp(Connection connection, Value input) {
        return timestamp(connection, input, LocalTime.MIN);
    }

    public static LocalDateTime timestamp(Connection connection, Value input, LocalTime time) {
        var temporal = valueToTemporal(connection, input);

        if (temporal instanceof LocalDate) {
            var localDate = (LocalDate) temporal;
            return LocalDateTime.of(localDate, time);
        }

        return LocalDateTime.from(temporal)
            .plusHours(time.getHour())
            .plusMinutes(time.getMinute())
            .plusSeconds(time.getSecond());
    }
}
