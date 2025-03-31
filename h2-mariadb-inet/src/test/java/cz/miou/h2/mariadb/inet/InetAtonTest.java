package cz.miou.h2.mariadb.inet;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class InetAtonTest {
    
    @ParameterizedTest
    @CsvSource({
        "'0.0.0.0', 0",
        "'192.168.1.1', 3232235777",
        "'255.255.255.255', 4294967295"
    })
    void testInetAton(String ip, long expected) throws SQLException {
        verifyQuery(
            "SELECT INET_ATON(?)",
            st -> st.setString(1, ip),
            rs -> assertThat(rs.getLong(1)).isEqualTo(expected)
        );
    }
    
}