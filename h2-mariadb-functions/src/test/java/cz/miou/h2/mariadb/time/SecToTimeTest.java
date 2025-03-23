package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class SecToTimeTest {

    @ParameterizedTest
    @CsvSource({
        "12414, '03:26:54'",
        "0, '00:00:00'",
        "86399, '23:59:59'"
    })
    void testMicrosecond(int input, String expected) throws SQLException {
        verifyQuery(
            "SELECT SEC_TO_TIME(?)",
            st -> st.setInt(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}