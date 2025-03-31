package cz.miou.h2.mariadb.binary;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class BitCountTest {

    @ParameterizedTest
    @CsvSource({
        "0, 0",
        "1, 1",
        "29, 4",
        "2147483647, 31",
        "9223372036854775807, 63"
    })
    void testBitCount(long input, int expected) throws SQLException {
        verifyQuery(
            "SELECT BIT_COUNT(?)",
            st -> st.setLong(1, input),
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

}