package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;

import java.sql.Connection;
import java.time.temporal.ChronoUnit;

import org.h2.value.Value;

import static cz.miou.h2.mariadb.time.DateTimeUtil.adjustDateTime;

/**
 * <a href="https://mariadb.com/kb/en/add_months/">ADD_MONTHS</a>
 */
public class AddMonths implements FunctionDefinition {

    @Override
    public String getName() {
        return "ADD_MONTHS";
    }

    @Override
    public String getMethodName() {
        return "addMonths";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value addMonths(Connection connection, Value date, double months) {
        return adjustDateTime(connection, date, temporal -> temporal.plus(Math.round(months), ChronoUnit.MONTHS));
    }

}
