package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonContainsTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{\"A\": 0, \"B\": {\"C\": 1}, \"D\": 2}', 2, '$.A', false",
        "'{\"A\": 0, \"B\": {\"C\": 1}, \"D\": 2}', 2, '$.D', true",
        "'{\"A\": 0, \"B\": {\"C\": 1}, \"D\": 2}', '{\"C\": 1}', '$.A', false",
        "'{\"A\": 0, \"B\": {\"C\": 1}, \"D\": 2}', '{\"C\": 1}', '$.B', true",

        "'[1, 2, 3]', 2, '$', true",
        "'[1, 2, 3]', 4, '$', false",
        "'[1, 2, 3]', '[]', '$', true",
        "'[1, 2, 3]', '[1]', '$', true",
        "'[1, 2, 3]', '[2]', '$', true",
        "'[1, 2, 3]', '[1, 2]', '$', true",
        "'[1, 2, 3]', '[1, 2, 3]', '$', true",
        "'[1, 2, 3]', '[4]', '$', false",
        "'[1, 2, 3]', '[1, 2, 4]', '$', false",
        "'[1, 2, 3]', '[1, 2, 3, 4]', '$', false",

        "'[{\"A\": 1}, {\"A\": 2}]', '{\"A\": 1}', '$', true",
        "'[{\"A\": 1}, {\"A\": 2}]', '{\"A\": 3}', '$', false",
    })
    void testJsonContains(String json, String val, String path, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_CONTAINS(?, ?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, val);
                st.setString(3, path);
            },
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }
    
}