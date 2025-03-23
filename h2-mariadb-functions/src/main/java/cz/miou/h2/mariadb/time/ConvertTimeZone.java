package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static cz.miou.h2.mariadb.time.DateTimeUtil.UNIX_TIME_END;
import static cz.miou.h2.mariadb.time.DateTimeUtil.UNIX_TIME_START;

/**
 * <a href="https://mariadb.com/kb/en/convert_tz/">CONVERT_TZ</a>
 */
public class ConvertTimeZone implements FunctionDefinition {

    @Override
    public String getName() {
        return "CONVERT_TZ";
    }

    @Override
    public String getMethodName() {
        return "convertTimeZone";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static LocalDateTime convertTimeZone(LocalDateTime date, String fromTz, String toTz) {
        if (date.isBefore(UNIX_TIME_START) || date.isAfter(UNIX_TIME_END)) {
            return date;
        }

        var from = parseTimeZone(fromTz);
        var to = parseTimeZone(toTz);

        return date
            .atZone(from)
            .withZoneSameInstant(to)
            .toLocalDateTime();
    }

    private static ZoneId parseTimeZone(String value) {
        try {
            return ZoneOffset.of(value);
        } catch (DateTimeException e) {
            return ZoneId.of(value);
        }
    }
}
