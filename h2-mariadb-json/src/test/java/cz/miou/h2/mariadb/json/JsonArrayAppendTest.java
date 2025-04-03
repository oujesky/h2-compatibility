package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonArrayAppendTest {
    
    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "|[1, 2, [3, 4]]|, |'$[0]', 5|, |[[1, 5], 2, [3, 4]]|",
        "|[1, 2, [3, 4]]|, |'$[1]', 6|, |[1, [2, 6], [3, 4]]|",
        "|[1, 2, [3, 4]]|, |'$[1]', 6, '$[2]', 7|, |[1, [2, 6], [3, 4, 7]]|",
        "|[1, 2, [3, 4]]|, |'$', 5|, |[1, 2, [3, 4], 5]|",

        "|{\"A\": 1, \"B\": [2], \"C\": [3, 4]}|, |'$.B', 5|, |{\"A\": 1, \"B\": [2, 5], \"C\": [3, 4]}|",
        "|{\"A\": 1, \"B\": [2], \"C\": [3, 4]}|, |'$.B[0]', 5|, |{\"A\": 1, \"B\": [[2, 5]], \"C\": [3, 4]}|",

        ", |'$', 123|, ",
        "|true|, |'$', 123|, |[true, 123]|",
        "1, |'$', 123|, |[1, 123]|",
        "|\"\"|, |'$', 123|, |[\"\", 123]|",
        "|[]|, |'$', 123|, |[123]|",

        "|[]|, |'$[0]'|, ",
        "|[1, 2]|, |'$[4]', 1|, ",
        "|[1, 2]|, |'$[*]', 1|, ",
    })
    void testJsonArrayAppend(String json, String values, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT JSON_ARRAY_APPEND(?, %s)", values),
            st -> st.setString(1, json),
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}