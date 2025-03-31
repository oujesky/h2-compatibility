package cz.miou.h2.mariadb.hash;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class Md5Test {
    
    @ParameterizedTest
    @CsvSource({
        "'testing', 'ae2b1fca515949e5d54fb22b8ed95575'",
        "'mariadb', '4aab37fabb3b33d71273effeab104bb7'"
    })
    void testMd5(String str, String expected) throws SQLException {
        verifyQuery(
            "SELECT MD5(?)",
            st -> st.setString(1, str),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}