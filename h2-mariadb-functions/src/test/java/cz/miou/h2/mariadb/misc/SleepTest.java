package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class SleepTest {

    @ParameterizedTest
    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    @CsvSource({
        "0, 0",
        "1, 0",
    })
    void testSleep(int duration, int expected) throws SQLException {
        verifyQuery(
            "SELECT SLEEP(?)",
            st -> st.setInt(1, duration),
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

}