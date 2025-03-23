package cz.miou.h2.mariadb.time;

import cz.miou.h2.api.FunctionDefinition;

import java.time.LocalDate;

/**
 * <a href="https://mariadb.com/kb/en/makedate/">MAKEDATE</a>
 */
public class MakeDate implements FunctionDefinition {

    @Override
    public String getName() {
        return "MAKEDATE";
    }

    @Override
    public String getMethodName() {
        return "makeDate";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static LocalDate makeDate(int year, int dayOfYear) {
        if (dayOfYear < 1) {
            return null;
        }

        return LocalDate.ofYearDay(year, dayOfYear);
    }
}
