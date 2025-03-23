package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.time.Duration;
import java.util.Objects;

import static cz.miou.h2.mariadb.time.DateTimeUtil.valueToTemporal;
import static org.h2.util.DateTimeUtils.NANOS_PER_SECOND;

/**
 * <a href="https://mariadb.com/kb/en/timediff/">TIMEDIFF</a>
 */
public class TimeDiff implements FunctionDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(TimeDiff.class);

    @Override
    public String getName() {
        return "TIMEDIFF";
    }

    @Override
    public String getMethodName() {
        return "timeDiff";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String timeDiff(Connection connection, Value expr1, Value expr2) {
        if (expr1.containsNull() || expr2.containsNull()) {
            return null;
        }

        var value1 = valueToTemporal(connection, expr1);
        var value2 = valueToTemporal(connection, expr2);

        if (!Objects.equals(value1.getClass(), value2.getClass())) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Both inputs should have same type but found {} and {}", value1.getClass().getName(), value2.getClass().getName());
            }
            return null;
        }

        var diff = Duration.between(value2, value1);

        var sign = diff.isNegative() ? "-" : "";
        diff =  diff.abs();

        var fraction = "";
        var nanos = diff.toNanosPart();
        if (nanos > 0) {
            fraction = String.format("%.06f", (double) nanos / NANOS_PER_SECOND);
            fraction = "." + fraction.substring(2, 8);
        }

        return String.format(
            "%s%02d:%02d:%02d%s",
            sign,
            diff.toHours(),
            diff.toMinutesPart(),
            diff.toSecondsPart(),
            fraction
        );

    }
}
