package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static cz.miou.h2.mariadb.time.DateTimeUtil.EPOCH_START_IN_SECONDS;
import static cz.miou.h2.mariadb.time.DateTimeUtil.valueToTemporal;

/**
 * <a href="https://mariadb.com/kb/en/to_seconds/">TO_SECONDS</a>
 */
public class ToSeconds implements FunctionDefinition {

    @Override
    public String getName() {
        return "TO_SECONDS";
    }

    @Override
    public String getMethodName() {
        return "toSeconds";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static long toSeconds(Connection connection, Value value) {
        var temporal = valueToTemporal(connection, value);
        var date = LocalDate.from(temporal);
        var time = temporal.isSupported(ChronoUnit.SECONDS) ? LocalTime.from(temporal) : LocalTime.MIN;
        var epochSecond = date.atTime(time).atZone(ZoneOffset.UTC).toEpochSecond();

        return epochSecond + EPOCH_START_IN_SECONDS;
    }
}
