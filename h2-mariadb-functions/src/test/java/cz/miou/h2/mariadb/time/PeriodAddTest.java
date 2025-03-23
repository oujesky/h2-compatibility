package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class PeriodAddTest {

    @ParameterizedTest
    @CsvSource({
        "200801, 2, 200803",
        "200801, 0, 200801",
        "200801, -1, 200712",
        "200800, 0, 200712",
        "200813, 0, 200901",
        "200899, 0, 201603",
        "200899, 5, 201608",
        "6910, 2, 206912",
        "7010, 2, 197012",
        "10, 2, 200012",
        "0, 0, 0",
        "1, 1, 200002",
        "-200001, 5, 200006"
    })
    void testPeriodAdd(int period, int months, int expected) throws SQLException {
        verifyQuery(
            "SELECT PERIOD_ADD(?, ?)",
            st -> {
                st.setInt(1, period);
                st.setInt(2, months);
            },
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

}