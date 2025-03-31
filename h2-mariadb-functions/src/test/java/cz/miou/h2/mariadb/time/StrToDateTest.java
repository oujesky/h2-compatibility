package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class StrToDateTest {

    @ParameterizedTest
    @CsvSource({
        "'Wednesday, June 2, 2014', '%W, %M %e, %Y', '2014-06-02'",
        "'22:23:00', '%H:%i:%s', '22:23:00'",
        "'2025-03-13 00:28:38.000069', '%Y-%m-%d %H:%i:%s.%f', '2025-03-13 00:28:38.000069'",
        "'01,5,2013','%d,%m,%Y', '2013-05-01'",
        "'May 1, 2013','%M %d,%Y', '2013-05-01'",
        "'09:30:17 AM','%h:%i:%s %p', '09:30:17'",
        "'09:30:17 PM','%h:%i:%s %p', '21:30:17'",
        "'a09:30:17','a%h:%i:%s', '09:30:17'",
        "'%09:30:17','%%%h:%i:%s', '09:30:17'",
        "'09:30:17a','%h:%i:%s', '09:30:17'",
//        "'200442 Monday', '%X%V %W', '2004-10-18'", // flaky
//        "'1998 52 Fri', '%X %V %a', '1999-01-01'", // flaky
        "'1998 53 5', '%x %v %w', '1999-01-01'",
        "'Sunday 4th of October 2009', '%W %D of %M %Y', '2009-10-04'",
        "'9 3 1', '%k %i %s', '09:03:01'",
        "'00 Oct 277', '%y %b %j', '2000-10-03'",
        "'10:23:00 PM', '%r', '22:23:00'",
        "'22:23:00', '%T', '22:23:00'",
        "'Wednesday23423, June 2, 2014', '%W%#, %M %e, %Y', '2014-06-02'",
        "'Wednesday.... , June 2, 2014', '%W%., %M %e, %Y', '2014-06-02'",
        "'Wednesday, June 2AbcDef, 2014', '%W, %M %e%@, %Y', '2014-06-02'",
    })
    void testStrToDate(String input, String format, String expected) throws SQLException {
        verifyQuery(
            "SELECT STR_TO_DATE(?, ?)",
            st -> {
                st.setString(1, input);
                st.setString(2, format);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'Wednesday23423, June 2, 2014', '%W, %M %e, %Y'",
        "'a09:30:17','%h:%i:%s'",
        "'00/00/0000', '%m/%d/%Y'",
        "'200442', '%X%V'",
        "'1999 00 Fri', '%Y %U %a'",
        "'1999 00 Fri', '%Y %u %a'",
    })
    void testStrToDateInvalid(String input, String format) throws SQLException {
        verifyQuery(
            "SELECT STR_TO_DATE(?, ?)",
            st -> {
                st.setString(1, input);
                st.setString(2, format);
            },
            rs -> assertThat(rs.getString(1)).isNull()
        );
    }



}