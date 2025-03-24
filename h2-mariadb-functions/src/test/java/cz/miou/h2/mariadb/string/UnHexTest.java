package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class UnHexTest {

    @ParameterizedTest
    @CsvSource({
        "4D617269614442, 'MariaDB'",
        "737472696E67, 'string'",
    })
    void testUnHex(String input, String expected) throws SQLException {
        verifyQuery(
            "SELECT UNHEX(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }


}