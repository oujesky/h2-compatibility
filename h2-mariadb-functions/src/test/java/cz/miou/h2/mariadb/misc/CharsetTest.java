package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.misc.Charset}.
 */
class CharsetTest {

    @ParameterizedTest
    @CsvSource(quoteCharacter = '"', value = {
        "'abc', utf8mb4",
        "X'4142', binary",
        "NULL, binary"
    })
    void testCharset(String expression, String expected) throws SQLException {
        verifyQuery(
            "SELECT CHARSET(" + expression + ")",
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
}
