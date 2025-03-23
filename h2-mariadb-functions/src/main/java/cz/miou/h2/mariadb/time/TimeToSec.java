package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;
import java.time.LocalTime;

import static cz.miou.h2.mariadb.time.DateTimeUtil.valueToTemporal;
import static org.h2.util.DateTimeUtils.NANOS_PER_SECOND;

/**
 * <a href="https://mariadb.com/kb/en/time_to_sec/">TIME_TO_SEC</a>
 */
public class TimeToSec implements FunctionDefinition {

    @Override
    public String getName() {
        return "TIME_TO_SEC";
    }

    @Override
    public String getMethodName() {
        return "timeToSec";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static double timeToSec(Connection connection, Value time) {
        var temporal = valueToTemporal(connection, time);
        var localTime = LocalTime.from(temporal);

        var seconds = localTime.toSecondOfDay();
        var fraction = (double) localTime.getNano() / NANOS_PER_SECOND;

        return seconds + fraction;
    }
}
