package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class StrCmpTest {

    @ParameterizedTest
    @CsvSource({
        "'text', 'text2', -1",
        "'text2', 'text', 1",
        "'text', 'text', 0"
    })
    void testStrCmp(String subject, String pattern, int expected) throws SQLException {
        verifyQuery(
            "SELECT STRCMP(?, ?)",
            st -> {
                st.setString(1, subject);
                st.setString(2, pattern);
            },
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

}