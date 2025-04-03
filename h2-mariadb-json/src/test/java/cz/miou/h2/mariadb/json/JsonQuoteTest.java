package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonQuoteTest {
    
    @ParameterizedTest
    @CsvSource({
        "'A', '\"A\"'",
        "'\\A\\', '\"\\\\A\\\\\"'",
        "'\"C\"',  '\"\\\"C\\\"\"'",
        "'ěščřžýáí',  '\"ěščřžýáí\"'",
    })
    void testJsonQuote(String json, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_QUOTE(?)",
            st -> st.setString(1, json),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "9, '\"A\\tB\"'",
        "10, '\"A\\nB\"'",
        "13, '\"A\\rB\"'",
        "0, '\"A\\u0000B\"'",
        "27, '\"A\\u001BB\"'",
    })
    void testJsonQuoteSpecialChars(int chr, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_QUOTE(CONCAT('A', CHAR(?), 'B'))",
            st -> st.setInt(1, chr),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}