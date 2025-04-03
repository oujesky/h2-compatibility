package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonValueTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{\"key1\":123}', '$.key1', '123'",
        "'{\"key1\": [1,2,3], \"key1\":123}', '$.key1', '123'",
        "'{\"key1\":\"60\\\" Table\", \"key2\":\"1\"}', '$.key1', '60\" Table'",
        "'{\"key1\":\"60\\\" Table\", \"key2\":\"1\"}', '$.key2', '1'",

        "'{\"key1\": [1,2,3]}', '$.key1', ",
        "'{\"key1\": {}', '$.key1', ",
        "'{\"key1\": null', '$.key1', ",
    })
    void testJsonValue(String json, String path, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_VALUE(?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, path);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}