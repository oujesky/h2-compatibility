package cz.miou.h2.mariadb.util;

import java.util.Locale;

public class LocaleUtil {

    private LocaleUtil() {}


    public static Locale findLocale(String locale) {
        if (locale == null) {
            return Locale.getDefault();
        }

        var split = locale.split("[-_]", 2);
        return split.length < 2
            ? new Locale(split[0])
            : new Locale(split[0], split[1]);
    }
}
