package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.jdbc.JdbcConnection;
import org.h2.value.ValueTimestamp;

import static org.h2.util.DateTimeUtils.NANOS_PER_DAY;
import static org.h2.util.DateTimeUtils.convertScale;

/**
 * <a href="https://mariadb.com/kb/en/utc_time/">UTC_TIME</a>
 */
public class UtcTimestamp implements FunctionDefinition {

    @Override
    public String getName() {
        return "UTC_TIMESTAMP";
    }

    @Override
    public String getMethodName() {
        return "utcTimestamp";
    }

    @SuppressWarnings("unused")
    public static ValueTimestamp utcTimestamp(JdbcConnection connection) {
        return utcTimestamp(connection, 0);
    }

    public static ValueTimestamp utcTimestamp(JdbcConnection connection, int precision) {
        var timestamp = connection.currentTimestamp();
        var dateValue = timestamp.getDateValue();
        var nanos = convertScale(timestamp.getTimeNanos(), precision, NANOS_PER_DAY);
        return ValueTimestamp.fromDateValueAndNanos(dateValue, nanos);
    }
}
