package cz.miou.h2.mariadb.string;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class HexTest {

    @ParameterizedTest
    @CsvSource({
        "0, 0",
        "1, 1",
        "10, A",
        "12, C",
        "255, FF",
        "2147483647, 7FFFFFFF"
    })
    void testHex(int input, String expected) throws SQLException {
        verifyQuery(
            "SELECT HEX(?)",
            st -> st.setInt(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'MariaDB', 4D617269614442",
        "'string', 737472696E67",
    })
    void testHexFromString(String input, String expected) throws SQLException {
        verifyQuery(
            "SELECT HEX(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @Test
    void testHexFromLong() throws SQLException {
        verifyQuery(
            "SELECT HEX(?)",
            st -> st.setLong(1, 1234567890123456789L),
            rs -> assertThat(rs.getString(1)).isEqualTo("112210F47DE98115")
        );
    }

    @Test
    void testHexFromDouble() throws SQLException {
        verifyQuery(
            "SELECT HEX(?)",
            st -> st.setDouble(1, 123.567),
            rs -> assertThat(rs.getString(1)).isEqualTo("7B")
        );
    }

}