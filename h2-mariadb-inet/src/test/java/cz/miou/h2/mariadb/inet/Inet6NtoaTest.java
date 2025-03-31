package cz.miou.h2.mariadb.inet;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class Inet6NtoaTest {
    
    @ParameterizedTest
    @CsvSource({
        "0x0A000101, '10.0.1.1'",
        "0x48F3000000000000D4321431BA23846F, '48f3::d432:1431:ba23:846f'",
    })
    void testInet6Ntoa(String expr, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT INET6_NTOA(%s)", expr),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}