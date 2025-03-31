package cz.miou.h2.mariadb.inet;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class Inet6AtonTest {
    
    @ParameterizedTest
    @CsvSource({
        "'10.0.1.1', '0a000101'",
        "'48f3::d432:1431:ba23:846f', '48f3000000000000d4321431ba23846f'"
    })
    void testInet6Aton(String ip, String expected) throws SQLException {
        verifyQuery(
            "SELECT RAWTOHEX(INET6_ATON(?))",
            st -> st.setString(1, ip),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}