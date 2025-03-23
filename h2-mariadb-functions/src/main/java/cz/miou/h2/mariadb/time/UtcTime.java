package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.jdbc.JdbcConnection;
import org.h2.value.ValueTime;

import static org.h2.util.DateTimeUtils.NANOS_PER_DAY;
import static org.h2.util.DateTimeUtils.convertScale;

/**
 * <a href="https://mariadb.com/kb/en/utc_time/">UTC_TIME</a>
 */
public class UtcTime implements FunctionDefinition {

    @Override
    public String getName() {
        return "UTC_TIME";
    }

    @Override
    public String getMethodName() {
        return "utcTime";
    }

    @SuppressWarnings("unused")
    public static ValueTime utcTime(JdbcConnection connection) {
        return utcTime(connection, 0);
    }

    public static ValueTime utcTime(JdbcConnection connection, int precision) {
        var nanos = connection.currentTimestamp().getTimeNanos();
        nanos = convertScale(nanos, precision, NANOS_PER_DAY);
        return ValueTime.fromNanos(nanos);
    }
}
