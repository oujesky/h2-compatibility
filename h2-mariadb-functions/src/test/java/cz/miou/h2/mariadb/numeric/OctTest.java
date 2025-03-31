package cz.miou.h2.mariadb.numeric;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class OctTest {

    @ParameterizedTest
    @CsvSource({
        "34, 42",
        "12, 14"
    })
    void testOct(int value, String expected) throws SQLException {
        verifyQuery(
            "SELECT OCT(?)",
            st -> st.setInt(1, value),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}