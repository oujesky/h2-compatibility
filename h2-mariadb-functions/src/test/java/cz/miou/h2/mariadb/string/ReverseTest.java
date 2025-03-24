package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class ReverseTest {

    @ParameterizedTest
    @CsvSource({
        "'', ''",
        "'desserts', 'stressed'",
    })
    void testReverse(String input, String expected) throws SQLException {
        verifyQuery(
            "SELECT REVERSE(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}