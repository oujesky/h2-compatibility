package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class AddDateTest {

    @ParameterizedTest
    @CsvSource({
        "'2008-01-02', 31, '2008-02-02'",
        "'2007-01-30 21:31:07', 10, '2007-02-09 21:31:07'",
        "'1983-10-15 06:42:51', 10, '1983-10-25 06:42:51'",
        "'2011-04-21 12:34:56', 10, '2011-05-01 12:34:56'",
        "'2011-10-30 06:31:41', 10, '2011-11-09 06:31:41'",
        "'2011-01-30 14:03:25', 10, '2011-02-09 14:03:25'",
        "'2004-10-07 11:19:34', 10, '2004-10-17 11:19:34'",
    })
    void testAddDateDays(String date, int days, String expected) throws SQLException {
        verifyQuery(
            "SELECT ADDDATE(?, ?)",
            st -> {
                st.setString(1, date);
                st.setInt(2, days);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'2008-01-02', 31, DAY, '2008-02-02'",
        "'2007-01-30 21:31:07', 10, HOUR, '2007-01-31 07:31:07'",
        "'1983-10-15 06:42:51', 10, HOUR, '1983-10-15 16:42:51'",
        "'2011-04-21 12:34:56', 10, HOUR, '2011-04-21 22:34:56'",
        "'2011-10-30 06:31:41', 10, HOUR, '2011-10-30 16:31:41'",
        "'2011-01-30 14:03:25', 10, HOUR, '2011-01-31 00:03:25'",
        "'2004-10-07 11:19:34', 10, HOUR, '2004-10-07 21:19:34'",
    })
    void testAddDateInterval(String date, int interval, String unit, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT ADDDATE(?, INTERVAL '%d' %s)", interval, unit),
            st -> st.setString(1, date),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}