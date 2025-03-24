package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class SubstringIndexTest {

    @ParameterizedTest
    @CsvSource({
        "'www.mariadb.org', '.', 1, 'www'",
        "'www.mariadb.org', '.', -1, 'org'",
        "'www.mariadb.org', '.', 2, 'www.mariadb'",
        "'www.mariadb.org', '.', -2, 'mariadb.org'",
        "'www.mariadb.org', '.', 0, ''",
        "'www.mariadb.org', '.', 5, 'www.mariadb.org'",
        "'www.mariadb.org', '.', -5, 'www.mariadb.org'",
    })
    void testMid(String str, String delim, int count, String expected) throws SQLException {
        verifyQuery(
            "SELECT SUBSTRING_INDEX(?, ?, ?)",
            st -> {
                st.setString(1, str);
                st.setString(2, delim);
                st.setInt(3, count);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
}
