package cz.miou.h2.mariadb.numeric;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class ConvTest {

    @ParameterizedTest
    @CsvSource({
        "'a', 16 , 2, '1010'",
        "'6E',18, 8, '172'",
        "'-17', 10, -18, '-H'",
    })
    void testConv(String input, int from, int to, String expected) throws SQLException {
        verifyQuery(
            "SELECT CONV(?, ?, ?)",
            st -> {
                st.setString(1, input);
                st.setInt(2, from);
                st.setInt(3, to);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}