package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonExistsTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{\"key1\":\"xxxx\", \"key2\":[1, 2, 3]}', '$.key2', true",
        "'{\"key1\":\"xxxx\", \"key2\":[1, 2, 3]}', '$.key3', false",
        "'{\"key1\":\"xxxx\", \"key2\":[1, 2, 3]}', '$.key2[1]', true",
        "'{\"key1\":\"xxxx\", \"key2\":[1, 2, 3]}', '$.key2[10]', false",
        "'{\"key1\":\"xxxx\", \"key2\":[1, 2, 3, null]}', '$.key2[3]', true",
    })
    void testJsonExists(String json, String path, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_EXISTS(?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, path);
            },
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }
    
}