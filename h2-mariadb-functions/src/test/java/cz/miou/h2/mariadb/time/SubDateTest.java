package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class SubDateTest {

    @ParameterizedTest
    @CsvSource({
        "'2008-01-02 12:00:00', 31, '2007-12-02 12:00:00'",
        "'2007-01-30 21:31:07', 10, '2007-01-20 21:31:07'",
        "'1983-10-15 06:42:51', 10, '1983-10-05 06:42:51'",
        "'2011-04-21 12:34:56', 10, '2011-04-11 12:34:56'",
        "'2011-10-30 06:31:41', 10, '2011-10-20 06:31:41'",
        "'2011-01-30 14:03:25', 10, '2011-01-20 14:03:25'",
        "'2004-10-07 11:19:34', 10, '2004-09-27 11:19:34'",
    })
    void testSubDateDays(String date, int days, String expected) throws SQLException {
        verifyQuery(
            "SELECT SUBDATE(?, ?)",
            st -> {
                st.setString(1, date);
                st.setInt(2, days);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'2008-01-02', 31, DAY, '2007-12-02'",
        "'2007-01-30 21:31:07', 10, MINUTE, '2007-01-30 21:21:07'",
        "'1983-10-15 06:42:51', 10, MINUTE, '1983-10-15 06:32:51'",
        "'2011-04-21 12:34:56', 10, MINUTE, '2011-04-21 12:24:56'",
        "'2011-10-30 06:31:41', 10, MINUTE, '2011-10-30 06:21:41'",
        "'2011-01-30 14:03:25', 10, MINUTE, '2011-01-30 13:53:25'",
        "'2004-10-07 11:19:34', 10, MINUTE, '2004-10-07 11:09:34'",
    })
    void testSubDateInterval(String date, int interval, String unit, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT SUBDATE(?, INTERVAL '%d' %s)", interval, unit),
            st -> st.setString(1, date),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}