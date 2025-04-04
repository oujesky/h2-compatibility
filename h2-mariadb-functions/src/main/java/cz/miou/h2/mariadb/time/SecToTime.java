package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.ValueTime;

import static org.h2.util.DateTimeUtils.NANOS_PER_SECOND;

/**
 * <a href="https://mariadb.com/kb/en/sec_to_time/">SEC_TO_TIME</a>
 * <p>
 * Differences to MariaDB:
 * <ul>
 *   <li>hour range is 0 to 23, as different values are not supported by H2 TIME data type
 * </ul>
 */
public class SecToTime implements FunctionDefinition {

    @Override
    public String getName() {
        return "SEC_TO_TIME";
    }

    @Override
    public String getMethodName() {
        return "secToTime";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static ValueTime secToTime(int seconds) {
        return ValueTime.fromNanos(seconds * NANOS_PER_SECOND);
    }
}
