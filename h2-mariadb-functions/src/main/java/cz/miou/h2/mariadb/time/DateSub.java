package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueInterval;

import java.sql.Connection;

import static cz.miou.h2.mariadb.time.DateTimeUtil.subtractInterval;

/**
 * <a href="https://mariadb.com/kb/en/date_sub/">DATE_SUB</a>
 * <p>
 * Differences to MariaDB:
 * <ul>
 *   <li>interval value needs to be quoted with apostrophes (e.g. INTERVAL '1' SECOND)
 *   <li>only YEAR, MONTH, DAY, HOUR, MINUTE, SECOND interval qualifiers are supported in H2
 * </ul>
 */
public class DateSub implements FunctionDefinition {

    @Override
    public String getName() {
        return "DATE_SUB";
    }

    @Override
    public String getMethodName() {
        return "dateSub";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value dateSub(Connection connection, Value date, ValueInterval interval) {
        return subtractInterval(connection, date, interval);
    }
}
