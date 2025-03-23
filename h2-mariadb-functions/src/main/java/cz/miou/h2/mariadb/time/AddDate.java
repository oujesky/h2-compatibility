package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueInterval;

import java.sql.Connection;
import java.time.temporal.ChronoUnit;

import static cz.miou.h2.mariadb.time.DateTimeUtil.addInterval;
import static cz.miou.h2.mariadb.time.DateTimeUtil.adjustDateTime;

/**
 * <a href="https://mariadb.com/kb/en/adddate/">ADDDATE</a>
 * <p>
 * Differences to MariaDB:
 * <ul>
 *   <li>interval value needs to be quoted with apostrophes (e.g. INTERVAL '1' SECOND)
 *   <li>only YEAR, MONTH, DAY, HOUR, MINUTE, SECOND interval qualifiers are supported in H2
 */
public class AddDate implements FunctionDefinition {

    @Override
    public String getName() {
        return "ADDDATE";
    }

    @Override
    public String getMethodName() {
        return "addDate";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value addDate(Connection connection, Value date, Value interval) {
        if (interval instanceof ValueInterval) {
            ValueInterval valueInterval = (ValueInterval) interval;
            return addInterval(connection, date, valueInterval);
        }

        return adjustDateTime(connection, date, temporal -> temporal.plus(interval.getInt(), ChronoUnit.DAYS));
    }
}
