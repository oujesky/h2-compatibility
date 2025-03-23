package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class TimeToSecTest {

    @ParameterizedTest
    @CsvSource({
        "'22:23:00', 80580",
        "'00:39:38', 2378",
        "'09:12:55.2355', 33175.2355"
    })
    void testTimeToSec(String input, double expected) throws SQLException {
        verifyQuery(
            "SELECT TIME_TO_SEC(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getDouble(1)).isEqualTo(expected)
        );
    }

}