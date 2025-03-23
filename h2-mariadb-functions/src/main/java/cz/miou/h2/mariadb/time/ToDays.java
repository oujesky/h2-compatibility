package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueDate;

import java.sql.Connection;

import static cz.miou.h2.mariadb.time.DateTimeUtil.EPOCH_START_IN_DAYS;
import static cz.miou.h2.mariadb.time.DateTimeUtil.convertToDateTimeType;
import static org.h2.util.DateTimeUtils.absoluteDayFromDateValue;

/**
 * <a href="https://mariadb.com/kb/en/to_days/">TO_DAYS</a>
 */
public class ToDays implements FunctionDefinition {

    @Override
    public String getName() {
        return "TO_DAYS";
    }

    @Override
    public String getMethodName() {
        return "toDays";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static long toDays(Connection connection, Value value) {
        value = convertToDateTimeType(connection, value);

        if (value instanceof ValueDate) {
            var date = (ValueDate) value;
            return EPOCH_START_IN_DAYS + absoluteDayFromDateValue(date.getDateValue());
        }

        throw new IllegalArgumentException("Expected DATE type, got " + value.getType().toString());
    }
}
