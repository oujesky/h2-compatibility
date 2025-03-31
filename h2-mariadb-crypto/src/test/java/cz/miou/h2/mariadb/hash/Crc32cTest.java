package cz.miou.h2.mariadb.hash;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class Crc32cTest {
    
    @ParameterizedTest
    @CsvSource({
        "'MariaDB', 809606978",
        "'mariadb', 1378644259"
    })
    void testCrc32c(String input, long expected) throws SQLException {
        verifyQuery(
            "SELECT CRC32C(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getLong(1)).isEqualTo(expected)
        );
    }

    @Test
    void testCrc32cPartial() throws SQLException {
        verifyQuery(
            "SELECT CRC32C(CRC32C('Maria'), 'DB')",
            rs -> assertThat(rs.getLong(1)).isEqualTo(809606978L)
        );
    }
    
}