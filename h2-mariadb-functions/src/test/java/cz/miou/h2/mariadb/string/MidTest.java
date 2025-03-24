package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class MidTest {

    @ParameterizedTest
    @CsvSource({
        "'abcd', 4, 1, 'd'",
        "'abcd', 4, 5, 'd'",
        "'abcd', 2, 2, 'bc'",
        "'abcd', -2, 4, 'cd'",
        "'abcd', 0, 4, ''",
        "'abcd', 5, 4, ''",
        "'abcd', -5, 4, ''",
    })
    void testMid(String str, int pos, int len, String expected) throws SQLException {
        verifyQuery(
            "SELECT MID(?, ?, ?)",
            st -> {
                st.setString(1, str);
                st.setInt(2, pos);
                st.setInt(3, len);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
}
