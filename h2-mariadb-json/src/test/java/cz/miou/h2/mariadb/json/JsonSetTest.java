package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonSetTest {
    
    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.C', '[3, 4]'|, |{ \"A\": 0, \"B\": [1, 2], \"C\":\"[3, 4]\"}|",
        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.C', '[3, 4]', '$.C', '[5, 6]'|, |{ \"A\": 0, \"B\": [1, 2], \"C\":\"[5, 6]\"}|",
        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.C', '[3, 4]', '$.D', '[5, 6]'|, |{ \"A\": 0, \"B\": [1, 2], \"C\":\"[3, 4]\", \"D\":\"[5, 6]\"}|",
        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.C', 123|, |{ \"A\": 0, \"B\": [1, 2], \"C\":123}|",

        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.C.D', 123|, |{ \"A\": 0, \"B\": [1, 2]}|",
        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.B', '[3, 4]'|, |{ \"A\": 0, \"B\": \"[3, 4]\"}|",

        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.B[0]', 3|, |{ \"A\": 0, \"B\": [1, 2]}|",
        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.B[2]', 3|, |{ \"A\": 0, \"B\": [1, 2, 3]}|",

        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$[0]', 3|, |{ \"A\": 0, \"B\": [1, 2]}|",
        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$[1]', 3|, |[{ \"A\": 0, \"B\": [1, 2]}, 3]|",
        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.A[0]', 3|, |{ \"A\": 0, \"B\": [1, 2]}|",
        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.A[1]', 3|, |{ \"A\": [0, 3], \"B\": [1, 2]}|",

        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.B', JSON_EXTRACT('[3, 4]', '$')|, |{ \"A\": 0, \"B\": [3, 4]}|",
        "|{ \"A\": 0, \"B\": [1, 2]}|, |'$.C', JSON_EXTRACT('[3, 4]', '$')|, |{ \"A\": 0, \"B\": [1, 2], \"C\": [3, 4]}|",
    })
    void testJsonSet(String json, String values, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT JSON_SET(?, %s)", values),
            st -> st.setString(1, json),
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}