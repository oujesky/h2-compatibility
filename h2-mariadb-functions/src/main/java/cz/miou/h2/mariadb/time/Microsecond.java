package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import java.sql.Connection;
import java.time.temporal.ChronoField;

import static cz.miou.h2.mariadb.time.DateTimeUtil.valueToTemporal;

/**
 * <a href="https://mariadb.com/kb/en/microsecond/">MICROSECOND</a>
 */
public class Microsecond implements FunctionDefinition {

    @Override
    public String getName() {
        return "MICROSECOND";
    }

    @Override
    public String getMethodName() {
        return "microsecond";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static int microsecond(Connection connection, Value value) {
        var temporal = valueToTemporal(connection, value);

        if (!temporal.isSupported(ChronoField.MICRO_OF_SECOND)) {
            return 0;
        }

        return temporal.get(ChronoField.MICRO_OF_SECOND);
    }
}
