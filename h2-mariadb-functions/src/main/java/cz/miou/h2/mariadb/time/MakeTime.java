package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;

import java.time.LocalTime;

/**
 * <a href="https://mariadb.com/kb/en/maketime/">MAKETIME</a>
 * <p>
 * Differences to MariaDB:
 * <ul>
 *   <li>hour range is 0 to 23, as different values are not supported by H2 TIME data type
 * </ul>
 */
public class MakeTime implements FunctionDefinition {
    @Override
    public String getName() {
        return "MAKETIME";
    }

    @Override
    public String getMethodName() {
        return "makeTime";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static LocalTime makeTime(int hour, int minute, int second) {
        if (minute < 0 || minute > 59 || second < 0 || second > 59) {
            return null;
        }

        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException(String.format("H2 TIME data type allows only values 0 - 23 for hour, %d supplied", hour));
        }

        return LocalTime.of(hour, minute, second);
    }
}
