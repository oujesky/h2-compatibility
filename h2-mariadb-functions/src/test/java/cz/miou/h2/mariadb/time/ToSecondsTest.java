package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class ToSecondsTest {

    @ParameterizedTest
    @CsvSource({
        "'2013-06-13', 63538300800",
        "'2013-06-13 21:45:13', 63538379113",

    })
    void testToSeconds(String input, long expected) throws SQLException {
        verifyQuery(
            "SELECT TO_SECONDS(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getLong(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "20130513, 63535622400",
        "130513, 63535622400"
    })
    void testToSecondsFromNumber(int input, long expected) throws SQLException {
        verifyQuery(
            "SELECT TO_SECONDS(?)",
            st -> st.setInt(1, input),
            rs -> assertThat(rs.getLong(1)).isEqualTo(expected)
        );
    }

}