package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonQueryTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{\"key1\":{\"a\":1, \"b\":[1,2]}}', '$.key1', '{\"a\":1, \"b\":[1,2]}'",
        "'{\"key1\":123, \"key1\": [1,2,3]}', '$.key1', '[1,2,3]'",
        "'{\"key1\":123, \"key1\": [1,2,3]}', '$.key2', ",
    })
    void testJsonQuery(String json, String path, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_QUERY(?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, path);
            },
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}