package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonOverlapsTest {
    
    @ParameterizedTest
    @CsvSource({
        "'false', 'false', true",
        "'false', 'true', false",
        "'123', '123', true",
        "'\"123\"', '\"123\"', true",
        "'\"123\"', '123', false",

        "'true', '[\"abc\", 1, 2, true, false]', true",
        "'[\"abc\", 1, 2, true, false]', 'true', true",

        "'[\"abc\", 1, 2]', '[\"xyz\", 3, 4]', false",
        "'[\"abc\", 1, 2]', '[\"abc\", 3, 4]', true",
        "'[\"abc\", 3, 2]', '[\"abc\", 3, 4]', true",

        "'{\"A\": 1, \"B\": {\"C\":2}}', '{\"A\": 2, \"B\": {\"C\":2}}', true",
        "'[1, 2, true, false, null]', '[3, 4, [1]]', false",
    })
    void testJsonOverlaps(String json1, String json2, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_OVERLAPS(?, ?)",
            st -> {
                st.setString(1, json1);
                st.setString(2, json2);
            },
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }
    
}