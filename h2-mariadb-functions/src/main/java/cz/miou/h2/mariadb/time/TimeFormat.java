package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;
import java.util.Set;

import static cz.miou.h2.mariadb.time.DateTimeFormatUtil.formatDateTime;

/**
 * <a href="https://mariadb.com/kb/en/time_format/">TIME_FORMAT</a>
 * <p>
 * Differences with MariaDB:
 * <ul>
 *   <li>H2 TIME value is strictly 00:00:00 to 23:59:59, whereas MariaDB allows negative values (e.g. -01:00:00) or
 *          values spilling to another day (e.g. 24:00:00)
 */
public class TimeFormat implements FunctionDefinition {

    private static final Set<String> ALLOWED_TOKENS = Set.of("%H", "%k", "%h", "%I", "%l");

    @Override
    public String getName() {
        return "TIME_FORMAT";
    }

    @Override
    public String getMethodName() {
        return "timeFormat";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String timeFormat(Connection connection, Value input, String format) {
        return formatDateTime(connection, input, format, null, ALLOWED_TOKENS);
    }
}
