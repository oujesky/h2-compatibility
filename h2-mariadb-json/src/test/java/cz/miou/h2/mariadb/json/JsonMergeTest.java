package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonMergeTest {
    
    @ParameterizedTest
    @CsvSource({
        "'null', 'null', '[null, null]'",
        "'1', 'null', '[1, null]'",
        "'1', '2', '[1, 2]'",

        "'{}', '{}', '{}'",
        "'{\"a\": {\"b\": {\"c\": 1}}}', '{\"a\": {\"b\": {\"c\": 2}}}', '{\"a\": {\"b\": {\"c\": [1, 2]}}}'",
        "'{\"a\": {\"b\": {\"c\": 1}}}', '{\"a\": {\"b\": {\"d\": 2}}}', '{\"a\": {\"b\": {\"c\": 1, \"d\": 2}}}'",
        "'{\"a\": {\"b\": {\"c\": 1}}}', '{\"aa\": {\"b\": {\"c\": 2}}}', '{\"a\": {\"b\": {\"c\": 1}}, \"aa\": {\"b\": {\"c\": 2}}}'",

        "'[]', '[]', '[]'",
        "'[1]', '[]', '[1]'",
        "'[]', '[1]', '[1]'",
        "'[1]', '[1]', '[1, 1]'",
        "'[1, 2]', '[2, 3]', '[1, 2, 2, 3]'",
        "'[{\"a\":1}]', '[{\"a\":1}]', '[{\"a\":1}, {\"a\":1}]",
        "'[{\"a\":1}]', '1', '[{\"a\":1}, 1]",
        "'[{\"a\":1}]', '[1]', '[{\"a\":1}, 1]",
        "'[1]', '[{\"a\":1}]', '[1, {\"a\":1}]",

        "'1', '[2]', '[1, 2]'",
        "'[1]', '2', '[1, 2]'",
        "'1', '{}', '[1, {}]'",
        "'[1]', '{}', '[1, {}]'",
        "'1', '[{}]', '[1, {}]'",
        "'[1]', '[{}]', '[1, {}]'",
    })
    void testJsonMerge(String json, String patch, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_MERGE(?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, patch);
            },
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "|{}|, |'{}', '{}'|, |{}|",
        "|[]|, |'[]', '[]'|, |[]|",
        "|1|, |'2', '3', '4', '5'|, |[1, 2, 3, 4, 5]|",
        "|{\"a\":1}|, |'{\"a\":2}', '{\"a\":3}', '{\"a\":4}'|, |{\"a\":[1, 2, 3, 4]}|",
        "|{\"a\":1}|, |'{\"b\":2}', '{\"c\":3}', '{\"d\":4}'|, |{\"a\":1, \"b\":2, \"c\":3, \"d\":4}|",
    })
    void testJsonMergeMultiple(String json, String patches, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT JSON_MERGE(?, %s)", patches),
            st -> st.setString(1, json),
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}