package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class MicrosecondTest {

    @ParameterizedTest
    @CsvSource({
        "'12:00:00.123456', 123456",
        "'2009-12-31 23:59:59.000010', 10",
        "'2013-08-07 12:13:14', 0",
        "'2013-08-07', 0"
    })
    void testMicrosecond(String input, int expected) throws SQLException {
        verifyQuery(
            "SELECT MICROSECOND(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

}