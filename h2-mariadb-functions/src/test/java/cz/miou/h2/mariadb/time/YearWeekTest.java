package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class YearWeekTest {

    @ParameterizedTest
    @CsvSource({
       "'1987-01-01', 198652",
        "'2007-01-30 21:31:07', 200704",
        "'1983-10-15 06:42:51', 198341",
        "'2011-04-21 12:34:56', 201116",
        "'2011-10-30 06:31:41', 201144",
        "'2011-01-30 14:03:25', 201105",
        "'2004-10-07 11:19:34', 200440",
    })
    void testYearWeek(String date, int expected) throws SQLException {
        verifyQuery(
            "SELECT YEARWEEK(?)",
            st -> st.setString(1, date),
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

}