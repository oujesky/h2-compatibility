package cz.miou.h2.mariadb.numeric;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class Log2Test {

    @ParameterizedTest
    @CsvSource({
        "4398046511104, 42",
        "65536, 16",
        "2, 1",
        "1, 0",
        "3, 1.5849625007211563",
        "34534, 15.075729827726907"
    })
    void testLog2(long input, double expected) throws SQLException {
        verifyQuery(
            "SELECT LOG2(?)",
            st -> st.setLong(1, input),
            rs -> assertThat(rs.getDouble(1)).isCloseTo(expected, Percentage.withPercentage(0.00001))
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -100})
    void testLog2Null(long input) throws SQLException {
        verifyQuery(
            "SELECT LOG2(?)",
            st -> st.setLong(1, input),
            rs -> {
                assertThat(rs.getInt(1)).isZero();
                assertThat(rs.wasNull()).isTrue();
            }
        );
    }

}