package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class DateFormatTest {

    @ParameterizedTest
    @CsvSource({
        "'2009-10-04 22:23:00', '%W %M %Y', 'Sunday October 2009'",
        "'2007-10-04 22:23:00', '%H:%i:%s', '22:23:00'",
        "'21:23:00.123456', '%h %l %f %p', '09 9 123456 PM'",
        "'09:23:00', '%k %f %% %p', '9 000000 % AM'",
        "'1900-10-04 22:23:00', '%D %y %a %d %m %b %j', '4th 00 Thu 04 10 Oct 277'",
        "'1997-10-04 22:23:00', '%H %k %I %r %T %S %w', '22 22 10 10:23:00 PM 22:23:00 00 6'",
        "'1999-01-01', '%X %V %c %e', '1998 52 1 1'",
        "'1999-01-01', '%x %v %U %u', '1998 53 00 00'",
        "'2023-09-20 15:00:23+02:00', '%W %d %M %Y %H:%i:%s %z', 'Wednesday 20 September 2023 15:00:23 +0200'",
        "'2004-10-18', '%X%V %W', '200442 Monday'",
    })
    void testDateFormat(String date, String format, String expected) throws SQLException {
        verifyQuery(
            "SELECT DATE_FORMAT(?, ?)",
            st -> {
                st.setString(1, date);
                st.setString(2, format);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'2009-10-04 22:23:00', '%W %M %Y', 'cs_CZ', 'neděle říjen 2009'",
        "'1900-10-04 22:23:00', '%a %b', 'de_DE', 'Do Okt'",
        "'2006-01-01', '%W', 'el_GR', 'Κυριακή'"
    })
    void testDateFormat(String date, String format, String language, String expected) throws SQLException {
        verifyQuery(
            "SELECT DATE_FORMAT(?, ?, ?)",
            st -> {
                st.setString(1, date);
                st.setString(2, format);
                st.setString(3, language);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }


}