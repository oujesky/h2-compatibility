package cz.miou.h2.mariadb.time;

import org.h2.api.IntervalQualifier;
import org.h2.engine.CastDataProvider;
import org.h2.value.Value;
import org.h2.value.ValueDate;
import org.h2.value.ValueInterval;
import org.h2.value.ValueTime;
import org.h2.value.ValueTimeTimeZone;
import org.h2.value.ValueTimestamp;
import org.h2.value.ValueTimestampTimeZone;

import java.sql.Connection;
import java.text.ParsePosition;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.function.UnaryOperator;

import static org.h2.util.DateTimeUtils.dateValue;
import static org.h2.util.JSR310Utils.localDateTimeToValue;
import static org.h2.util.JSR310Utils.localDateToValue;
import static org.h2.util.JSR310Utils.localTimeToValue;
import static org.h2.util.JSR310Utils.offsetDateTimeToValue;
import static org.h2.util.JSR310Utils.offsetTimeToValue;
import static org.h2.util.JSR310Utils.valueToLocalDate;
import static org.h2.util.JSR310Utils.valueToLocalDateTime;
import static org.h2.util.JSR310Utils.valueToLocalTime;
import static org.h2.util.JSR310Utils.valueToOffsetDateTime;
import static org.h2.util.JSR310Utils.valueToOffsetTime;
import static org.h2.util.JSR310Utils.zonedDateTimeToValue;

class DateTimeUtil {

    public static final LocalDateTime UNIX_TIME_START = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
    public static final LocalDateTime UNIX_TIME_END = LocalDateTime.of(2038, 1, 19, 5, 14, 7);

    public static final int EPOCH_START_IN_DAYS = 719528; // 1970-01-01
    public static final long EPOCH_START_IN_SECONDS = 62167219200L; // 1970-01-01 00:00:00

    private static final DateTimeFormatter TIME_EXPRESSION_FORMATTER = new DateTimeFormatterBuilder()
        .appendPattern("[d ]H:m:s")
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .toFormatter();

    private DateTimeUtil() {}

    public static Value convertToDateTimeType(Connection connection, Value value) {
        if (value.getValueType() == Value.DATE
            || value.getValueType() == Value.TIME || value.getValueType() == Value.TIME_TZ
            || value.getValueType() == Value.TIMESTAMP || value.getValueType() == Value.TIMESTAMP_TZ) {
            return value;
        }

        if (value.getValueType() == Value.VARCHAR) {
            var string = value.getString();

            if (!hasTimeAndDate(string)) {
                return string.indexOf(':') < 0
                    ? ValueDate.parse(string)
                    : ValueTime.parse(string, (CastDataProvider) connection);
            }

            return hasTimeZoneInfo(string)
                ? ValueTimestampTimeZone.parse(string, (CastDataProvider) connection)
                : ValueTimestamp.parse(string, (CastDataProvider) connection);
        }

        if (value.getValueType() == Value.INTEGER) {
            var date = value.getInt();

            var year = date / 10000;
            if (year < 70) {
                year += 2000;
            } else if (year < 100) {
                year += 1900;
            }

            var month = date % 10000 / 100;
            var day = date % 100;

            return ValueDate.fromDateValue(dateValue(year, month, day));
        }

        throw new IllegalArgumentException("Only DATE, DATETIME, TIME or TIMESTAMP types are supported");
    }

    private static boolean hasTimeZoneInfo(String string) {
        return string.endsWith("Z")
               || string.indexOf('+') > -1
               || string.lastIndexOf('-') > string.lastIndexOf(':')
               || string.indexOf('[') > -1
               || string.lastIndexOf(' ') > string.indexOf(' ');
    }

    private static boolean hasTimeAndDate(String string) {
        return string.indexOf(' ') > -1
               || string.indexOf('T') > -1;
    }

    public static Temporal valueToTemporal(Connection connection, Value value) {
        value = convertToDateTimeType(connection, value);
        var provider = (CastDataProvider) connection;

        if (value instanceof ValueTime) {
            return valueToLocalTime(value, provider);
        }

        if (value instanceof ValueTimeTimeZone) {
            return valueToOffsetTime(value, provider);
        }

        if (value instanceof ValueDate) {
            return valueToLocalDate(value, provider);
        }

        if (value instanceof ValueTimestamp) {
            return valueToLocalDateTime(value, provider);
        }

        if (value instanceof ValueTimestampTimeZone) {
            return valueToOffsetDateTime(value, provider);
        }

        throw new IllegalArgumentException("Only DATE, DATETIME, TIME or TIMESTAMP types are supported");
    }

    public static Value temporalToValue(Temporal temporal) {
        if (temporal instanceof LocalTime) {
            var localTime = (LocalTime) temporal;
            return localTimeToValue(localTime);
        }

        if (temporal instanceof OffsetTime) {
            var offsetTime = (OffsetTime) temporal;
            return offsetTimeToValue(offsetTime);
        }

        if (temporal instanceof LocalDate) {
            LocalDate localDate = (LocalDate) temporal;
            return localDateToValue(localDate);
        }

        if (temporal instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) temporal;
            return localDateTimeToValue(localDateTime);
        }

        if (temporal instanceof OffsetDateTime) {
            OffsetDateTime offsetDateTime = (OffsetDateTime) temporal;
            return offsetDateTimeToValue(offsetDateTime);
        }

        if (temporal instanceof ZonedDateTime) {
            ZonedDateTime zonedDateTime = (ZonedDateTime) temporal;
            return zonedDateTimeToValue(zonedDateTime);
        }

        throw new IllegalArgumentException(String.format("Unsupported temporal type: %s", temporal.getClass().getName()));
    }

    public static Value adjustDateTime(Connection connection, Value value, UnaryOperator<Temporal> adjustment) {
        var temporal = valueToTemporal(connection, value);
        var adjusted = adjustment.apply(temporal);
        return temporalToValue(adjusted);
    }

    public static Value addInterval(Connection connection, Value value, ValueInterval interval) {
        return adjustDateInterval(connection, value, interval, Temporal::plus);
    }

    public static Value subtractInterval(Connection connection, Value value, ValueInterval interval) {
        return adjustDateInterval(connection, value, interval, Temporal::minus);
    }

    private static Value adjustDateInterval(Connection connection, Value date, ValueInterval interval, TemporalAdjustment adjustment) {
        var value = interval.getLong();
        var qualifier = interval.getQualifier();

        var unit = getChronoUnit(qualifier);

        return adjustDateTime(connection, date, temporal -> adjustment.apply(temporal, value, unit));
    }

    private static ChronoUnit getChronoUnit(IntervalQualifier qualifier) {
        switch (qualifier) {
            case YEAR:
                return ChronoUnit.YEARS;
            case MONTH:
                return ChronoUnit.MONTHS;
            case DAY:
                return ChronoUnit.DAYS;
            case HOUR:
                return ChronoUnit.HOURS;
            case MINUTE:
                return ChronoUnit.MINUTES;
            case SECOND:
                return ChronoUnit.SECONDS;
            default:
                throw new IllegalArgumentException(String.format("Unsupported INTERVAL qualifier '%s'", qualifier));
        }
    }

    public static Duration parseTimeExpression(String value) {
        var parsed = TIME_EXPRESSION_FORMATTER.parseUnresolved(value, new ParsePosition(0));

        var duration = Duration.ZERO;
        if (parsed.isSupported(ChronoField.NANO_OF_SECOND)) {
            duration = duration.plusNanos(parsed.getLong(ChronoField.NANO_OF_SECOND));
        }
        if (parsed.isSupported(ChronoField.MILLI_OF_SECOND)) {
            duration = duration.plusMillis(parsed.getLong(ChronoField.MILLI_OF_SECOND));
        }
        if (parsed.isSupported(ChronoField.SECOND_OF_MINUTE)) {
            duration = duration.plusSeconds(parsed.get(ChronoField.SECOND_OF_MINUTE));
        }
        if (parsed.isSupported(ChronoField.MINUTE_OF_HOUR)) {
            duration = duration.plusMinutes(parsed.get(ChronoField.MINUTE_OF_HOUR));
        }
        if (parsed.isSupported(ChronoField.HOUR_OF_DAY)) {
            duration = duration.plusHours(parsed.get(ChronoField.HOUR_OF_DAY));
        }
        if (parsed.isSupported(ChronoField.DAY_OF_MONTH)) {
            duration = duration.plusDays(parsed.get(ChronoField.DAY_OF_MONTH));
        }

        return duration;
    }

    public static YearMonth parsePeriod(int period) {
        return parsePeriod(period, 0);
    }

    public static YearMonth parsePeriod(int period, int add) {
        period = Math.abs(period);

        int year = period / 100;
        if (year < 70) {
            year += 2000;
        } else if (year < 100) {
            year += 1900;
        }

        int month = period % 100 + add;

        if (month == 0) {
            month = 12;
            year--;
        }

        if (month > 12) {
            year += month / 12;
            month = (month % 12);
        }

        return YearMonth.of(year, month);
    }

    @FunctionalInterface
    public interface TemporalAdjustment {
        Temporal apply(Temporal temporal, long value, TemporalUnit unit);
    }
}
