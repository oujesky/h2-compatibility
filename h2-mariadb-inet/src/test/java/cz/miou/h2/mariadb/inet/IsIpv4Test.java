package cz.miou.h2.mariadb.inet;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class IsIpv4Test {
    
    @ParameterizedTest
    @CsvSource({
        "'255.255.255.255', true",
        "'1110.0.1.1', false",
        "'48f3::d432:1431:ba23:846f', false"
    })
    void testIsIpv4(String expr, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT IS_IPV4(?)",
            st -> st.setString(1, expr),
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }
    
}