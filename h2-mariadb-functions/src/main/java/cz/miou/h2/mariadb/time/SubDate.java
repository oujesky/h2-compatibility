package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueInterval;

import java.sql.Connection;
import java.time.temporal.ChronoUnit;

import static cz.miou.h2.mariadb.time.DateTimeUtil.adjustDateTime;
import static cz.miou.h2.mariadb.time.DateTimeUtil.subtractInterval;

/**
 * <a href="https://mariadb.com/kb/en/subdate/">SUBDATE</a>
 * <p>
 * Differences to MariaDB:
 * <ul>
 *   <li>interval value needs to be quoted with apostrophes (e.g. INTERVAL '1' SECOND)
 *   <li>only YEAR, MONTH, DAY, HOUR, MINUTE, SECOND interval qualifiers are supported
 */
public class SubDate implements FunctionDefinition {

    @Override
    public String getName() {
        return "SUBDATE";
    }

    @Override
    public String getMethodName() {
        return "subDate";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value subDate(Connection connection, Value date, Value interval) {
        if (interval instanceof ValueInterval) {
            ValueInterval valueInterval = (ValueInterval) interval;
            return subtractInterval(connection, date, valueInterval);
        }

        return adjustDateTime(connection, date, temporal -> temporal.minus(interval.getInt(), ChronoUnit.DAYS));
    }
}
