package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonObjectToArrayTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{\"a\": [1, 2, 3], \"b\": { \"key1\":\"val1\", \"key2\": {\"key3\":\"val3\"} }}', '[[\"a\", [1, 2, 3]], [\"b\", {\"key1\": \"val1\", \"key2\": {\"key3\": \"val3\"}}]]'",
        "'{\"a\": null}', '[[\"a\", null]]'",

        "'{}', ",
        "'123', ",
        "'null', ",
    })
    void testJsonObjectToArray(String json, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_OBJECT_TO_ARRAY(?)",
            st -> st.setString(1, json),
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}