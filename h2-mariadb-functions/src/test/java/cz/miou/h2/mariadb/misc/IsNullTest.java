package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class IsNullTest {
    
    @ParameterizedTest
    @CsvSource({
        "'1+1', false",
        "'NULL', true",
    })
    void testIsNull(String expr, boolean expected) throws SQLException {
        verifyQuery(
            String.format("SELECT ISNULL(%s)", expr),
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }
    
}