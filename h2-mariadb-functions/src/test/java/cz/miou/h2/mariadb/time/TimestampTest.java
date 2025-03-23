package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class TimestampTest {

    @ParameterizedTest
    @CsvSource({
        "'2003-12-31', '2003-12-31 00:00:00'",
        "'2003-12-31 12:00:00', '2003-12-31 12:00:00'",
        "'2003-12-31 12:00:00.000123', '2003-12-31 12:00:00.000123'"
    })
    void testTimestamp(String input, String expected) throws SQLException {
        verifyQuery(
            "SELECT TIMESTAMP(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'2003-12-31', '6:30:00', '2003-12-31 06:30:00'",
        "'2003-12-31 12:00:00', '6:30:00', '2003-12-31 18:30:00'",
        "'2003-12-31 12:00:00', '12:30:00', '2004-01-01 00:30:00'",
        "'2003-12-31 12:00:00.000123', '6:30:12', '2003-12-31 18:30:12.000123'"
    })
    void testTimestampWithTime(String input, String time, String expected) throws SQLException {
        verifyQuery(
            "SELECT TIMESTAMP(?, ?)",
            st -> {
                st.setString(1, input);
                st.setString(2, time);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}