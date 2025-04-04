package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;

import static cz.miou.h2.mariadb.time.DateTimeFormatUtil.formatDateTime;

/**
 * <a href="https://mariadb.com/kb/en/date_format/">DATE_FORMAT</a>
 * <p>
 * Differences to MariaDB:
 * <ul>
 *   <li>doesn't work with "invalid" dates otherwise accepted in MariaDB (e.g. 2006-06-00)
 *   <li>'%Z' token - time zone abbreviation is not supported as H2 keeps just time offset, not specific time zone
 * </ul>
 */
public class DateFormat implements FunctionDefinition {

    @Override
    public String getName() {
        return "DATE_FORMAT";
    }

    @Override
    public String getMethodName() {
        return "dateFormat";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String dateFormat(Connection connection, Value date, String format) {
        return dateFormat(connection, date, format, null);
    }

    public static String dateFormat(Connection connection, Value date, String format, String language) {
        return formatDateTime(connection, date, format, language, null);
    }

}
