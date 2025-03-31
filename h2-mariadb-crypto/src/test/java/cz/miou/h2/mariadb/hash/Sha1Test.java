package cz.miou.h2.mariadb.hash;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class Sha1Test {
    
    @ParameterizedTest
    @CsvSource({
        "'some boring text', 'af969fc2085b1bb6d31e517d5c456def5cdd7093'",
        "'mariadb', '378e3bcac6c74262787a896950a32586db5edb9f'"
    })
    void testSha1(String str, String expected) throws SQLException {
        verifyQuery(
            "SELECT SHA1(?)",
            st -> st.setString(1, str),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'some boring text', 'af969fc2085b1bb6d31e517d5c456def5cdd7093'",
        "'mariadb', '378e3bcac6c74262787a896950a32586db5edb9f'"
    })
    void testSha(String str, String expected) throws SQLException {
        verifyQuery(
            "SELECT SHA(?)",
            st -> st.setString(1, str),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}