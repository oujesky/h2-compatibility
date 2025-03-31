package cz.miou.h2.mariadb.numeric;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class PowTest {
    
    @ParameterizedTest
    @CsvSource({
        "2, 3, 8",
        "2, -2, 0.25"
    })
    void testPow(double x, double y, double expected) throws SQLException {
        verifyQuery(
            "SELECT POW(?, ?)",
            st -> {
                st.setDouble(1, x);
                st.setDouble(2, y);
            },
            rs -> assertThat(rs.getDouble(1)).isEqualTo(expected)
        );
    }
    
}