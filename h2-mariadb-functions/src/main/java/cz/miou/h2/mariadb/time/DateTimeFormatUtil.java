package cz.miou.h2.mariadb.time;

import org.h2.value.Value;

import java.sql.Connection;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import static cz.miou.h2.mariadb.time.DateTimeUtil.valueToTemporal;

class DateTimeFormatUtil {

    public static final TemporalField FIELD_WEEK_OF_YEAR_SUNDAY = WeekFields.of(DayOfWeek.SUNDAY, 4).weekOfYear();
    public static final TemporalField FIELD_WEEK_OF_YEAR_MONDAY = WeekFields.of(DayOfWeek.MONDAY, 4).weekOfYear();
    public static final TemporalField FIELD_WEEK_OF_WEEK_BASED_YEAR_SUNDAY = WeekFields.of(DayOfWeek.SUNDAY, 4).weekOfWeekBasedYear();
    public static final TemporalField FIELD_WEEK_OF_WEEK_BASED_YEAR_MONDAY = WeekFields.of(DayOfWeek.MONDAY, 4).weekOfWeekBasedYear();
    public static final TemporalField FIELD_WEEK_BASED_YEAR_SUNDAY = WeekFields.of(DayOfWeek.SUNDAY, 4).weekBasedYear();
    public static final TemporalField FIELD_WEEK_BASED_YEAR_MONDAY = WeekFields.of(DayOfWeek.MONDAY, 4).weekBasedYear();

    private static final Map<String, DateTimeFormatter> OUTPUT_FORMATTERS = Map.ofEntries(
        Map.entry("%a", DateTimeFormatter.ofPattern("ccc")),
        Map.entry("%b", DateTimeFormatter.ofPattern("LLL")),
        Map.entry("%c", DateTimeFormatter.ofPattern("M")),
        Map.entry("%D", DateTimeFormatter.ofPattern("d['st']['nd']['rd']['th']")),
        Map.entry("%d", DateTimeFormatter.ofPattern("dd")),
        Map.entry("%e", DateTimeFormatter.ofPattern("d")),
        Map.entry("%f", DateTimeFormatter.ofPattern("SSSSSS")),
        Map.entry("%H", DateTimeFormatter.ofPattern("HH")),
        Map.entry("%h", DateTimeFormatter.ofPattern("hh")),
        Map.entry("%I", DateTimeFormatter.ofPattern("hh")),
        Map.entry("%i", DateTimeFormatter.ofPattern("mm")),
        Map.entry("%j", DateTimeFormatter.ofPattern("DDD")),
        Map.entry("%k", DateTimeFormatter.ofPattern("H")),
        Map.entry("%l", DateTimeFormatter.ofPattern("h")),
        Map.entry("%M", DateTimeFormatter.ofPattern("LLLL")),
        Map.entry("%m", DateTimeFormatter.ofPattern("MM")),
        Map.entry("%p", DateTimeFormatter.ofPattern("a")),
        Map.entry("%r", DateTimeFormatter.ofPattern("hh:mm:ss a")),
        Map.entry("%S", DateTimeFormatter.ofPattern("ss")),
        Map.entry("%s", DateTimeFormatter.ofPattern("ss")),
        Map.entry("%T", DateTimeFormatter.ofPattern("HH:mm:ss")),
        Map.entry("%W", DateTimeFormatter.ofPattern("cccc")),
        Map.entry("%w", prepareFormatter(ChronoField.DAY_OF_WEEK, 1)),
        Map.entry("%U", prepareFormatter(FIELD_WEEK_OF_YEAR_SUNDAY, 2)),
        Map.entry("%u", prepareFormatter(FIELD_WEEK_OF_YEAR_MONDAY, 2)),
        Map.entry("%V", prepareFormatter(FIELD_WEEK_OF_WEEK_BASED_YEAR_SUNDAY, 2)),
        Map.entry("%v", prepareFormatter(FIELD_WEEK_OF_WEEK_BASED_YEAR_MONDAY, 2)),
        Map.entry("%X", prepareFormatter(FIELD_WEEK_BASED_YEAR_SUNDAY, 4)),
        Map.entry("%x", prepareFormatter(FIELD_WEEK_BASED_YEAR_MONDAY, 4)),
        Map.entry("%Y", DateTimeFormatter.ofPattern("yyyy")),
        Map.entry("%y", DateTimeFormatter.ofPattern("yy")),
        Map.entry("%z", DateTimeFormatter.ofPattern("xx")),
        Map.entry("%%", DateTimeFormatter.ofPattern("'%'"))
    );

    public static final Map<String, DateTimeFormatter> INPUT_FORMATTERS = Map.ofEntries(
        Map.entry("%a", DateTimeFormatter.ofPattern("ccc")),
        Map.entry("%b", DateTimeFormatter.ofPattern("LLL")),
        Map.entry("%c", DateTimeFormatter.ofPattern("M")),
        Map.entry("%D", DateTimeFormatter.ofPattern("d['st']['nd']['rd']['th']")),
        Map.entry("%d", DateTimeFormatter.ofPattern("d")),
        Map.entry("%e", DateTimeFormatter.ofPattern("d")),
        Map.entry("%f", DateTimeFormatter.ofPattern("SSSSSS")),
        Map.entry("%H", DateTimeFormatter.ofPattern("H")),
        Map.entry("%h", DateTimeFormatter.ofPattern("h")),
        Map.entry("%I", DateTimeFormatter.ofPattern("h")),
        Map.entry("%i", DateTimeFormatter.ofPattern("m")),
        Map.entry("%j", DateTimeFormatter.ofPattern("DDD")),
        Map.entry("%k", DateTimeFormatter.ofPattern("H")),
        Map.entry("%l", DateTimeFormatter.ofPattern("h")),
        Map.entry("%M", DateTimeFormatter.ofPattern("LLLL")),
        Map.entry("%m", DateTimeFormatter.ofPattern("M")),
        Map.entry("%p", DateTimeFormatter.ofPattern("a")),
        Map.entry("%r", DateTimeFormatter.ofPattern("hh:mm:ss a")),
        Map.entry("%S", DateTimeFormatter.ofPattern("s")),
        Map.entry("%s", DateTimeFormatter.ofPattern("s")),
        Map.entry("%T", DateTimeFormatter.ofPattern("HH:mm:ss")),
        Map.entry("%W", DateTimeFormatter.ofPattern("cccc")),
        Map.entry("%w", prepareFormatter(ChronoField.DAY_OF_WEEK, 1)),
        Map.entry("%U", prepareFormatter(FIELD_WEEK_OF_YEAR_SUNDAY, 2)),
        Map.entry("%u", prepareFormatter(FIELD_WEEK_OF_YEAR_MONDAY, 2)),
        Map.entry("%V", prepareFormatter(FIELD_WEEK_OF_WEEK_BASED_YEAR_SUNDAY, 2)),
        Map.entry("%v", prepareFormatter(FIELD_WEEK_OF_WEEK_BASED_YEAR_MONDAY, 2)),
        Map.entry("%X", prepareFormatter(FIELD_WEEK_BASED_YEAR_SUNDAY, 4)),
        Map.entry("%x", prepareFormatter(FIELD_WEEK_BASED_YEAR_MONDAY, 4)),
        Map.entry("%Y", DateTimeFormatter.ofPattern("yyyy")),
        Map.entry("%y", DateTimeFormatter.ofPattern("yy")),
        Map.entry("%z", DateTimeFormatter.ofPattern("xx")),
        Map.entry("%%", DateTimeFormatter.ofPattern("%"))
    );

    private static final Map<String, Function<Temporal, String>> FORMAT_FUNCTIONS = Map.of(
        "%D", DateTimeFormatUtil::dayNumberWithSuffix
    );

    private static final Pattern FORMAT_TOKEN_PATTERN = Pattern.compile("%(?<letter>[a-zA-Z%])?");

    private DateTimeFormatUtil() {}

    public static String formatDateTime(Connection connection, Value input, String format, String language, Set<String> allowedTokens) {
        var temporal = valueToTemporal(connection, input);
        var locale = findLocale(language);

        var sb = new StringBuilder();
        var matcher = FORMAT_TOKEN_PATTERN.matcher(format);
        while (matcher.find()) {
            var token = matcher.group();

            if (FORMAT_FUNCTIONS.containsKey(token)) {
                var function = FORMAT_FUNCTIONS.get(token);
                matcher.appendReplacement(sb, function.apply(temporal));
            } else if (OUTPUT_FORMATTERS.containsKey(token)) {
                if (allowedTokens != null && !allowedTokens.contains(token)) {
                    matcher.appendReplacement(sb, "0");
                }
                var formatter = OUTPUT_FORMATTERS.get(token).withLocale(locale);
                matcher.appendReplacement(sb, formatter.format(temporal));
            } else {
                matcher.appendReplacement(sb, matcher.group("letter"));
            }
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private static DateTimeFormatter prepareFormatter(TemporalField field, int width) {
        return new DateTimeFormatterBuilder()
            .appendValue(field, width)
            .toFormatter();
    }

    private static Locale findLocale(String locale) {
        if (locale == null) {
            return Locale.getDefault();
        }

        var split = locale.split("[-_]", 2);
        return split.length < 2
            ? new Locale(split[0])
            : new Locale(split[0], split[1]);
    }

    private static String dayNumberWithSuffix(Temporal temporal) {
        if (!temporal.isSupported(ChronoField.DAY_OF_MONTH)) {
            return "";
        }

        var day = temporal.get(ChronoField.DAY_OF_MONTH);

        String suffix;
        switch (day) {
            case 1:
            case 21:
            case 31:
                suffix = "st";
                break;
            case 2:
            case 22:
                suffix = "nd";
                break;
            case 3:
            case 23:
                suffix = "rd";
                break;
            default:
                suffix = "th";
                break;
        }

        return String.format("%d%s", day, suffix);
    }
}
