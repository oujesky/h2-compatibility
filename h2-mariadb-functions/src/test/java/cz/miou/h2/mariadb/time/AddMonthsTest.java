package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class AddMonthsTest {

    @ParameterizedTest
    @CsvSource({
        "'2012-01-31', 2, '2012-03-31'",
        "'2012-01-31', -5, '2011-08-31'",
        "'2011-01-31', 1, '2011-02-28'",
        "'2012-01-31', 1, '2012-02-29'",
        "'2012-01-31', 2, '2012-03-31'",
        "'2012-01-31', 3, '2012-04-30'",
        "'2011-01-15', 2.5, '2011-04-15'",
        "'2011-01-15', 2.6, '2011-04-15'",
        "'2011-01-15', 2.1, '2011-03-15'",
    })
    void testAddMonthsDate(String date, double month, String expected) throws SQLException {
        verifyQuery(
            "SELECT ADD_MONTHS(?, ?)",
            st -> {
                st.setString(1, date);
                st.setDouble(2, month);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'2012-01-31 12:34:56', 2, '2012-03-31 12:34:56'",
        "'2012-01-31 12:34:56', -5, '2011-08-31 12:34:56'",
        "'2011-01-31 12:34:56', 1, '2011-02-28 12:34:56'",
        "'2012-01-31 12:34:56', 1, '2012-02-29 12:34:56'",
        "'2012-01-31 12:34:56', 2, '2012-03-31 12:34:56'",
        "'2012-01-31 12:34:56', 3, '2012-04-30 12:34:56'",
        "'2011-01-15 12:34:56', 2.5, '2011-04-15 12:34:56'",
        "'2011-01-15 12:34:56', 2.6, '2011-04-15 12:34:56'",
        "'2011-01-15 12:34:56', 2.1, '2011-03-15 12:34:56'",
    })
    void testAddMonthsDateTime(String dateTime, double month, String expected) throws SQLException {
        verifyQuery(
            "SELECT ADD_MONTHS(?, ?)",
            st -> {
                st.setString(1, dateTime);
                st.setDouble(2, month);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }


}