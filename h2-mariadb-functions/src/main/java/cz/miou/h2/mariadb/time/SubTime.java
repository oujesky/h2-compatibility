package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;

import static cz.miou.h2.mariadb.time.DateTimeUtil.adjustDateTime;
import static cz.miou.h2.mariadb.time.DateTimeUtil.parseTimeExpression;

/**
 * <a href="https://mariadb.com/kb/en/subtime/">SUBTIME</a>
 * <p>
 * Differences with MariaDB:
 * <ul>
 *   <li>H2 TIME value is strictly 00:00:00 to 23:59:59, whereas MariaDB allows negative values (e.g. -01:00:00) or
 *          values spilling to another day (e.g. 24:00:00)
 * </ul>
 */
public class SubTime implements FunctionDefinition {

    @Override
    public String getName() {
        return "SUBTIME";
    }

    @Override
    public String getMethodName() {
        return "subTime";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value subTime(Connection connection, Value expr1, String expr2) {
        var duration = parseTimeExpression(expr2);
        return adjustDateTime(connection, expr1, duration::subtractFrom);
    }

}
