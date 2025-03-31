package cz.miou.h2.mariadb.hash;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class Crc32Test {
    
    @ParameterizedTest
    @CsvSource({
        "'MariaDB', 4227209140",
        "'mariadb', 2594253378"
    })
    void testCrc32(String input, long expected) throws SQLException {
        verifyQuery(
            "SELECT CRC32(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getLong(1)).isEqualTo(expected)
        );
    }

    @Test
    void testCrc32Partial() throws SQLException {
        verifyQuery(
            "SELECT CRC32(CRC32('Maria'), 'DB')",
            rs -> assertThat(rs.getLong(1)).isEqualTo(4227209140L)
        );
    }
}