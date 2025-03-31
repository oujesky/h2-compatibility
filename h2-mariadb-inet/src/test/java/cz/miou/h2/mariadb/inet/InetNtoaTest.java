package cz.miou.h2.mariadb.inet;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class InetNtoaTest {
    
    @ParameterizedTest
    @CsvSource({
        "0, '0.0.0.0'",
        "3232235777, '192.168.1.1'",
        "4294967295, '255.255.255.255'",
    })
    void testInetNtoa(long expr, String expected) throws SQLException {
        verifyQuery(
            "SELECT INET_NTOA(?)",
            st -> st.setLong(1, expr),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}