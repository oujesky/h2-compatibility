package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class WeekDayTest {

    @ParameterizedTest
    @CsvSource({
        "'2008-02-03 22:23:00', 6",
        "'2007-11-06', 1",
        "'2011-10-30 06:31:41', 6",
        "'2011-01-30 14:03:25', 6",
    })
    void testWeekDay(String date, int expected) throws SQLException {
        verifyQuery(
            "SELECT WEEKDAY(?)",
            st -> st.setString(1, date),
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

}