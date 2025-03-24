package cz.miou.h2.mariadb.string;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class BinTest {

    @ParameterizedTest
    @CsvSource({
        "0, 0",
        "1, 1",
        "2, 10",
        "12, 1100",
        "2147483647, 1111111111111111111111111111111"
    })
    void testBin(int input, String expected) throws SQLException {
        verifyQuery(
            "SELECT BIN(?)",
            st -> st.setInt(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @Test
    void testBinFromLong() throws SQLException {
        verifyQuery(
            "SELECT BIN(?)",
            st -> st.setLong(1, 1234567890123456789L),
            rs -> assertThat(rs.getString(1)).isEqualTo("1000100100010000100001111010001111101111010011000000100010101")
        );
    }

    @Test
    void testBinFromDouble() throws SQLException {
        verifyQuery(
            "SELECT BIN(?)",
            st -> st.setDouble(1, 123.567),
            rs -> assertThat(rs.getString(1)).isEqualTo("1111011")
        );
    }

}