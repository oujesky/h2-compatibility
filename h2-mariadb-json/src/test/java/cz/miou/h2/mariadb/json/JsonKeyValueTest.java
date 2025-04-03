package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonKeyValueTest {

    @ParameterizedTest
    @CsvSource({
        "'[[1, {\"key1\":\"val1\", \"key2\":\"val2\"}, 3], 2, 3]', '$[0][1]', '[{\"key\": \"key1\", \"value\": \"val1\"}, {\"key\": \"key2\", \"value\": \"val2\"}]'",
        "'[[1, {\"key1\":{\"inner\":\"val1\"}, \"key2\":\"val2\"}, 3], 2, 3]', '$[0][1]', '[{\"key\": \"key1\", \"value\": {\"inner\":\"val1\"}}, {\"key\": \"key2\", \"value\": \"val2\"}]'",
        "'[[1, {}, 3], 2, 3]', '$[0][1]', '[]'",
        "'[[1, {\"key1\":\"val1\", \"key2\":\"val2\"}, 3], 2, 3]', '$[0][2]', ",
        "'[[1, {\"key1\":\"val1\", \"key2\":\"val2\"}, 3], 2, 3]', '$', ",
    })
    void testJsonKeyValue(String json, String path, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_KEY_VALUE(?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, path);
            },
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }

}