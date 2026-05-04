package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.misc.Coercibility}.
 */
class CoercibilityTest {

    @ParameterizedTest
    @CsvSource(quoteCharacter = '"', value = {
        "'abc', 4",
        "42, 5",
        "NULL, 6"
    })
    void testCoercibility(String expression, int expected) throws SQLException {
        verifyQuery(
            "SELECT COERCIBILITY(" + expression + ")",
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }
}
