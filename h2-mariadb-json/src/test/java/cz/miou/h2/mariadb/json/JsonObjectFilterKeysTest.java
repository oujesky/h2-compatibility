package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonObjectFilterKeysTest {

    @ParameterizedTest
    @CsvSource({
        "'{\"a\": 1, \"b\": 2, \"c\": 3}', '[\"a\"]', '{\"a\": 1}'",
        "'{\"a\": 1, \"b\": 2, \"c\": 3}', '[\"b\"]', '{\"b\": 2}'",
        "'{\"a\": 1, \"b\": 2, \"c\": 3}', '[\"a\", \"a\"]', '{\"a\": 1}'",
        "'{\"a\": 1, \"b\": 2, \"c\": 3}', '[\"a\", \"x\"]', '{\"a\": 1}'",

        "'{\"a\": 1, \"b\": 2, \"c\": 3}', '[\"a\", \"b\"]', '{\"a\": 1, \"b\": 2}'",
        "'{\"a\": 1, \"b\": 2, \"c\": 3}', '[\"b\", \"a\"]', '{\"a\": 1, \"b\": 2}'",
        "'{\"a\": 1, \"b\": 2, \"c\": 3}', '[\"a\", \"b\", \"x\"]', '{\"a\": 1, \"b\": 2}'",

        "'{\"a\": 1, \"b\": 2, \"c\": 3}', '\"a\"', ",
        "'{\"a\": 1, \"b\": 2, \"c\": 3}', 'a', ",
        "'{\"a\": 1, \"b\": 2, \"c\": 3}', '[]', ",
        "'{\"a\": 1, \"b\": 2, \"c\": 3}', '[\"x\"]', ",

        "'{}', '[\"a\"]', ",
        "'[\"a\"]', '[\"a\"]', ",
        "'\"a\"', '[\"a\"]', ",
        "'null', '[\"a\"]', ",
    })
    void testJsonObjectFilterKeys(String json, String keys, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_OBJECT_FILTER_KEYS(?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, keys);
            },
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }

}