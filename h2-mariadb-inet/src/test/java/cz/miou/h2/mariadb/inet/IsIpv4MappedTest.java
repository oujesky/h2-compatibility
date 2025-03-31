package cz.miou.h2.mariadb.inet;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class IsIpv4MappedTest {
    
    @ParameterizedTest
    @CsvSource({
        "'::10.0.1.1', false",
        "'::ffff:10.0.1.1', true"
    })
    void testIsIpv4Mapped(String expr, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT IS_IPV4_MAPPED(INET6_ATON(?))",
            st -> st.setString(1, expr),
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }
    
}