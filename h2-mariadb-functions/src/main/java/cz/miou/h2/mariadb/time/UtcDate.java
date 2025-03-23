package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.jdbc.JdbcConnection;
import org.h2.value.ValueDate;

/**
 * <a href="https://mariadb.com/kb/en/utc_date/">UTC_DATE</a>
 */
public class UtcDate implements FunctionDefinition {

    @Override
    public String getName() {
        return "UTC_DATE";
    }

    @Override
    public String getMethodName() {
        return "utcDate";
    }

    @SuppressWarnings("unused")
    public static ValueDate utcDate(JdbcConnection connection) {
        return connection.currentTimestamp().convertToDate(connection);
    }
}
