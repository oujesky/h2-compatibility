package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueInterval;

import java.sql.Connection;

import static cz.miou.h2.mariadb.time.DateTimeUtil.addInterval;

/**
 * <a href="https://mariadb.com/kb/en/date_add/">DATE_ADD</a>
 * <p>
 * Differences to MariaDB:
 * <ul>
 *   <li>interval value needs to be quoted with apostrophes (e.g. INTERVAL '1' SECOND)
 *   <li>only YEAR, MONTH, DAY, HOUR, MINUTE, SECOND interval qualifiers are supported in H2
 */
public class DateAdd implements FunctionDefinition {

    @Override
    public String getName() {
        return "DATE_ADD";
    }

    @Override
    public String getMethodName() {
        return "dateAdd";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value dateAdd(Connection connection, Value date, ValueInterval interval) {
        return addInterval(connection, date, interval);
    }
}
