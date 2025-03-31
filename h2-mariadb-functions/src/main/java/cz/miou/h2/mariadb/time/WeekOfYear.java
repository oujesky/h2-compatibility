package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;
import java.time.temporal.WeekFields;

import static cz.miou.h2.mariadb.time.DateTimeUtil.valueToTemporal;

/**
 * <a href="https://mariadb.com/kb/en/weekofyear/">WEEKOFYEAR</a>
 */
public class WeekOfYear implements FunctionDefinition {

    @Override
    public String getName() {
        return "WEEKOFYEAR";
    }

    @Override
    public String getMethodName() {
        return "weekOfYear";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static int weekOfYear(Connection connection, Value value) {
        var temporal = valueToTemporal(connection, value);
        return temporal.get(WeekFields.SUNDAY_START.weekBasedYear());
    }

}
