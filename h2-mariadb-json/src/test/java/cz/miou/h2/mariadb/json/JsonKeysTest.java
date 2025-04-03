package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonKeysTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{\"A\": 1, \"B\": {\"C\": 2}}', '$', '[\"A\", \"B\"]'",
        "'{\"A\": 1, \"B\": 2, \"C\": {\"D\": 3}}', '$.C', '[\"D\"]'",
        "'{\"A\": 1, \"B\": 2, \"C\": {}}', '$.C', '[]'",
        "'{\"A\": 1, \"B\": 2, \"C\": {\"D\": 3}}', '$.A', ",
    })
    void testJsonKeys(String json, String path, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_KEYS(?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, path);
            },
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}