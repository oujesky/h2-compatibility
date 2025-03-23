package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static cz.miou.h2.mariadb.time.DateTimeFormatUtil.FIELD_WEEK_BASED_YEAR_MONDAY;
import static cz.miou.h2.mariadb.time.DateTimeFormatUtil.FIELD_WEEK_BASED_YEAR_SUNDAY;
import static cz.miou.h2.mariadb.time.DateTimeFormatUtil.FIELD_WEEK_OF_WEEK_BASED_YEAR_MONDAY;
import static cz.miou.h2.mariadb.time.DateTimeFormatUtil.FIELD_WEEK_OF_WEEK_BASED_YEAR_SUNDAY;
import static cz.miou.h2.mariadb.time.DateTimeFormatUtil.FIELD_WEEK_OF_YEAR_MONDAY;
import static cz.miou.h2.mariadb.time.DateTimeFormatUtil.FIELD_WEEK_OF_YEAR_SUNDAY;
import static cz.miou.h2.mariadb.time.DateTimeFormatUtil.INPUT_FORMATTERS;
import static org.h2.util.JSR310Utils.localDateTimeToValue;
import static org.h2.util.JSR310Utils.localDateToValue;
import static org.h2.util.JSR310Utils.localTimeToValue;

/**
 * <a href="https://mariadb.com/kb/en/str_to_date/">STR_TO_DATE</a>
 * <p>
 * Differences to MariaDB:
 * <ul>
 *   <li>'%Z' token - time zone abbreviation is not supported as H2 keeps just time offset, not specific time zone
 *   <li>'%U' and '%u' token - week of year is not supported as Java week number doesn't support 0th week
 *   <li>doesn't work with "invalid" / zero dates otherwise accepted in MariaDB (e.g. 0000-00-00)
 */
public class StrToDate implements FunctionDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(StrToDate.class);

    private static final Map<String, Set<TemporalField>> FIELD_MAP = Map.ofEntries(
        Map.entry("%a", Set.of(ChronoField.DAY_OF_WEEK)),
        Map.entry("%b", Set.of(ChronoField.MONTH_OF_YEAR)),
        Map.entry("%c", Set.of(ChronoField.MONTH_OF_YEAR)),
        Map.entry("%D", Set.of(ChronoField.DAY_OF_MONTH)),
        Map.entry("%d", Set.of(ChronoField.DAY_OF_MONTH)),
        Map.entry("%e", Set.of(ChronoField.DAY_OF_MONTH)),
        Map.entry("%f", Set.of(ChronoField.NANO_OF_SECOND)),
        Map.entry("%H", Set.of(ChronoField.HOUR_OF_DAY)),
        Map.entry("%h", Set.of(ChronoField.HOUR_OF_DAY, ChronoField.HOUR_OF_AMPM)),
        Map.entry("%I", Set.of(ChronoField.HOUR_OF_DAY, ChronoField.HOUR_OF_AMPM)),
        Map.entry("%i", Set.of(ChronoField.MINUTE_OF_HOUR)),
        Map.entry("%j", Set.of(ChronoField.DAY_OF_YEAR)),
        Map.entry("%k", Set.of(ChronoField.HOUR_OF_DAY)),
        Map.entry("%l", Set.of(ChronoField.HOUR_OF_DAY, ChronoField.HOUR_OF_AMPM)),
        Map.entry("%M", Set.of(ChronoField.MONTH_OF_YEAR)),
        Map.entry("%m", Set.of(ChronoField.MONTH_OF_YEAR)),
        Map.entry("%p", Set.of(ChronoField.AMPM_OF_DAY)),
        Map.entry("%r", Set.of(ChronoField.HOUR_OF_AMPM, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE, ChronoField.AMPM_OF_DAY)),
        Map.entry("%S", Set.of(ChronoField.SECOND_OF_MINUTE)),
        Map.entry("%s", Set.of(ChronoField.SECOND_OF_MINUTE)),
        Map.entry("%T", Set.of(ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE)),
        Map.entry("%W", Set.of(ChronoField.DAY_OF_WEEK)),
        Map.entry("%w", Set.of(ChronoField.DAY_OF_WEEK)),
        Map.entry("%U", Set.of(FIELD_WEEK_OF_YEAR_SUNDAY)),
        Map.entry("%u", Set.of(FIELD_WEEK_OF_YEAR_MONDAY)),
        Map.entry("%V", Set.of(FIELD_WEEK_OF_WEEK_BASED_YEAR_SUNDAY)),
        Map.entry("%v", Set.of(FIELD_WEEK_OF_WEEK_BASED_YEAR_MONDAY)),
        Map.entry("%X", Set.of(FIELD_WEEK_BASED_YEAR_SUNDAY)),
        Map.entry("%x", Set.of(FIELD_WEEK_BASED_YEAR_MONDAY)),
        Map.entry("%Y", Set.of(ChronoField.YEAR)),
        Map.entry("%y", Set.of(ChronoField.YEAR)),
        Map.entry("%z", Set.of())
    );

    private static final Pattern FORMAT_PATTERN = Pattern.compile("(%[a-zA-Z%#.@])|.");
    private static final Pattern SPACE_NORMALIZER = Pattern.compile("( +)");
    private static final Pattern PUNCTUATION = Pattern.compile("[\\p{Punct}\\p{IsPunctuation}]");

    @Override
    public String getName() {
        return "STR_TO_DATE";
    }

    @Override
    public String getMethodName() {
        return "strToDate";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value strToDate(String input, String format) {
        try {
            return parseInput(input, format);
        } catch (DateTimeException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Error parsing date input '{}' (format '{}'): {}", input, format, e.getMessage());
            }
            return ValueNull.INSTANCE;
        }
    }

    private static Value parseInput(String input, String format) {
        var fields = new HashMap<TemporalField, Integer>();
        var position = new ParsePosition(0);

        input = normalizeSpaces(input);
        format = normalizeSpaces(format);

        var matcher = FORMAT_PATTERN.matcher(format);
        var split = new ArrayList<String>();
        while (matcher.find()) {
            split.add(matcher.group());
        }

        for (var part : split) {
            if (part.isEmpty()) {
                continue;
            }

            var shouldSkip = skipPart(input, part, position);
            if (shouldSkip) {
                continue;
            }

            processPart(input, part, position, fields);
        }

        return createOutput(fields);
    }

    private static boolean skipPart(String input, String part, ParsePosition position) {
        switch (part) {
            case "%%":
                skip(input, position, c -> c == '%');
                return true;
            case "%#":
                skip(input, position, Character::isDigit);
                return true;
            case "%.":
                skip(input, position, StrToDate::isPunctuation);
                return true;
            case "%@":
                skip(input, position, Character::isAlphabetic);
                return true;
            default:
                return false;
        }
    }

    private static void processPart(String input, String part, ParsePosition position, Map<TemporalField, Integer> fields) {
        var formatter = createPartFormatter(part);
        var component = formatter.parse(input, position);

        var availableFields = FIELD_MAP.getOrDefault(part, Set.of());

        for (var field : availableFields) {
            if (!component.isSupported(field)) {
                continue;
            }

            var value = component.get(field);

            if (fields.containsKey(field)) {
                var original = fields.get(field);
                if (original > 0 && !original.equals(value)) {
                    throw new IllegalArgumentException(String.format("Mismatch value in field %s (%s) - new: %d, previous: %d", field, part, value, original));
                }
            }

            fields.put(field, value);
        }
    }

    private static String normalizeSpaces(String input) {
        return SPACE_NORMALIZER.matcher(input).replaceAll(" ");
    }

    private static Value createOutput(HashMap<TemporalField, Integer> fields) {
        var date = findDate(fields);
        var time = findTime(fields);

        if (date.isPresent() && time.isPresent()) {
            return localDateTimeToValue(LocalDateTime.of(date.get(), time.get()));
        }

        if (date.isPresent()) {
            return localDateToValue(date.get());
        }

        if (time.isPresent()) {
            return localTimeToValue(time.get());
        }

        return ValueNull.INSTANCE;
    }

    private static boolean isPunctuation(char c) {
        return PUNCTUATION.matcher(Character.toString(c)).matches();
    }

    private static void skip(String input, ParsePosition position, Predicate<Character> predicate) {
        var length = input.length();
        for (var i = position.getIndex(); i < length; i++) {
            if (!predicate.test(input.charAt(i))) {
                position.setIndex(i);
                break;
            }
        }
        if (Character.isSpaceChar(input.charAt(position.getIndex()))) {
            position.setIndex(position.getIndex() + 1);
        }
    }

    private static Optional<LocalTime> findTime(Map<TemporalField, Integer> fields) {
        if (!isTime(fields)) {
            return Optional.empty();
        }

        var hour = Optional.ofNullable(fields.get(ChronoField.HOUR_OF_DAY))
            .orElseGet(() -> {
                var amPm = fields.getOrDefault(ChronoField.AMPM_OF_DAY, 0);
                return fields.get(ChronoField.HOUR_OF_AMPM) + (amPm * 12);
            });

        var time = LocalTime.of(
            hour,
            fields.get(ChronoField.MINUTE_OF_HOUR),
            fields.get(ChronoField.SECOND_OF_MINUTE),
            fields.getOrDefault(ChronoField.NANO_OF_SECOND, 0)
        );

        return Optional.of(time);
    }

    private static Optional<LocalDate> findDate(Map<TemporalField, Integer> fields) {
        if (isDate(fields)) {
            var date = LocalDate.of(
                fields.get(ChronoField.YEAR),
                fields.get(ChronoField.MONTH_OF_YEAR),
                fields.get(ChronoField.DAY_OF_MONTH)
            );

            return Optional.of(date);
        }

        if (isDayOfYearDate(fields)) {
            var date = LocalDate.ofYearDay(
                fields.get(ChronoField.YEAR),
                fields.get(ChronoField.DAY_OF_YEAR)
            );

            return Optional.of(date);
        }

        if (isWeekDate(fields)) {
            if (fields.containsKey(FIELD_WEEK_OF_WEEK_BASED_YEAR_SUNDAY)) {
                return Optional.of(createDateFromWeek(fields, FIELD_WEEK_BASED_YEAR_SUNDAY, FIELD_WEEK_OF_WEEK_BASED_YEAR_SUNDAY, 1));
            }

            if (fields.containsKey(FIELD_WEEK_OF_WEEK_BASED_YEAR_MONDAY)) {
                return Optional.of(createDateFromWeek(fields, FIELD_WEEK_BASED_YEAR_MONDAY, FIELD_WEEK_OF_WEEK_BASED_YEAR_MONDAY, 0));
            }

            if (fields.containsKey(FIELD_WEEK_OF_YEAR_SUNDAY)) {
                return Optional.of(createDateFromWeek(fields, ChronoField.YEAR, FIELD_WEEK_OF_YEAR_SUNDAY, 1));
            }

            if (fields.containsKey(FIELD_WEEK_OF_YEAR_MONDAY)) {
                return Optional.of(createDateFromWeek(fields, ChronoField.YEAR, FIELD_WEEK_OF_YEAR_MONDAY, 0));
            }
        }

        return Optional.empty();
    }

    private static LocalDate createDateFromWeek(Map<TemporalField, Integer> fields, TemporalField yearField, TemporalField weekField, long offset) {
        return LocalDate.now()
            .with(yearField, fields.get(yearField))
            .with(weekField, fields.get(weekField) + offset)
            .with(ChronoField.DAY_OF_WEEK, fields.get(ChronoField.DAY_OF_WEEK));
    }

    private static boolean isTime(Map<TemporalField, Integer> fields) {
        return (fields.containsKey(ChronoField.HOUR_OF_DAY) || fields.containsKey(ChronoField.HOUR_OF_AMPM))
                && fields.containsKey(ChronoField.MINUTE_OF_HOUR)
                && fields.containsKey(ChronoField.SECOND_OF_MINUTE);
    }

    private static boolean isDate(Map<TemporalField, Integer> fields) {
        return fields.containsKey(ChronoField.YEAR)
               && fields.containsKey(ChronoField.MONTH_OF_YEAR)
               && fields.containsKey(ChronoField.DAY_OF_MONTH);
    }

    private static boolean isDayOfYearDate(Map<TemporalField, Integer> fields) {
        return fields.containsKey(ChronoField.YEAR)
               && fields.containsKey(ChronoField.DAY_OF_YEAR);
    }

    private static boolean isWeekDate(Map<TemporalField, Integer> fields) {
        return fields.containsKey(ChronoField.DAY_OF_WEEK)
            && (
                (fields.containsKey(FIELD_WEEK_BASED_YEAR_SUNDAY) && fields.containsKey(FIELD_WEEK_OF_WEEK_BASED_YEAR_SUNDAY))
                || (fields.containsKey(ChronoField.YEAR) && fields.containsKey(FIELD_WEEK_OF_YEAR_SUNDAY))
                || (fields.containsKey(FIELD_WEEK_BASED_YEAR_MONDAY) && fields.containsKey(FIELD_WEEK_OF_WEEK_BASED_YEAR_MONDAY))
                || (fields.containsKey(ChronoField.YEAR) && fields.containsKey(FIELD_WEEK_OF_YEAR_MONDAY))
               );
    }
    
    private static DateTimeFormatter createPartFormatter(String part) {
        if (INPUT_FORMATTERS.containsKey(part)) {
            return new DateTimeFormatterBuilder()
                .optionalStart()
                .appendLiteral(' ')
                .optionalEnd()
                .append(INPUT_FORMATTERS.get(part))
                .toFormatter();
        }

        return new DateTimeFormatterBuilder()
            .appendLiteral(part)
            .toFormatter();
    }


}
