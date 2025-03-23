package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class PeriodDiffTest {

    @ParameterizedTest
    @CsvSource({
        "200802, 200703, 11",
        "200802, 200802, 0",
        "6902, 6803, 11",
        "7002, 6803, -1177"
    })
    void testPeriodDiff(int period1, int period2, int expected) throws SQLException {
        verifyQuery(
            "SELECT PERIOD_DIFF(?, ?)",
            st -> {
                st.setInt(1, period1);
                st.setInt(2, period2);
            },
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

}