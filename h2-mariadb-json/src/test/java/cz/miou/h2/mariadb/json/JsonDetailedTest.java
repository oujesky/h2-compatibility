package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonDetailedTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{ \"A\":1,\"B\":[2,3]}', 4, '{\n" +
        "    \"A\": 1,\n" +
        "    \"B\": \n" +
        "    [\n" +
        "        2,\n" +
        "        3\n" +
        "    ]\n" +
        "}'",
        "'{\"C\":{\"X\":[]}}', 2, '{\n" +
        "  \"C\": \n" +
        "  {\n" +
        "    \"X\": \n" +
        "    []\n" +
        "  }\n" +
        "}'",
    })
    void testJsonDetailed(String json, int tabSize, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_DETAILED(?, ?)",
            st -> {
                st.setString(1, json);
                st.setInt(2, tabSize);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'{ \"A\":1,\"B\":[2,3]}', 4, '{\n" +
        "    \"A\": 1,\n" +
        "    \"B\": \n" +
        "    [\n" +
        "        2,\n" +
        "        3\n" +
        "    ]\n" +
        "}'",
        "'{\"C\":{\"X\":[]}}', 2, '{\n" +
        "  \"C\": \n" +
        "  {\n" +
        "    \"X\": \n" +
        "    []\n" +
        "  }\n" +
        "}'",
    })
    void testJsonPretty(String json, int tabSize, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_PRETTY(?, ?)",
            st -> {
                st.setString(1, json);
                st.setInt(2, tabSize);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}