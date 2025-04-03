package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonArrayInsertTest {
    
    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "|[1, 2, [3, 4]]|, |'$[0]', 5|, |[5, 1, 2, [3, 4]]|",
        "|[1, 2, [3, 4]]|, |'$[1]', 6|, |[1, 6, 2, [3, 4]]|",
        "|[1, 2, [3, 4]]|, |'$[2][1]', 6|, |[1, 2, [3, 6, 4]]|",
        "|[1, 2, [3, 4]]|, |'$[1]', 6, '$[2]', 7|, |[1, 6, 7, 2, [3, 4]]|",

        "|[1, 2, 3]|, |'$[-1]', 6|, |[1, 2, 3, 6]|",
        "|[1, 2, 3]|, |'$[-2]', 6|, |[1, 2, 6, 3]|",
        "|[1, 2, 3]|, |'$[-3]', 6|, |[1, 6, 2, 3]|",
        "|[1, 2, 3]|, |'$[-4]', 6|, |[6, 1, 2, 3]|",
        "|[1, 2, 3]|, |'$[-5]', 6|, |[1, 2, 3, 6]|",

        "|[1, 2, [3, 4]]|, |'$[1]', 6|, |[1, 6, 2, [3, 4]]|",

        "|{\"A\":[1, 2]}|, |'$.A[1]', 'x'|, |{\"A\": [1, \"x\", 2]}|",

        "|[]|, |'$[1]', 6|, |[6]|",
        "|[]|, |'$[5]', 6|, |[6]|",

        "|1|, |'$[0]', 123|, |1|",
    })
    void testJsonArrayInsert(String json, String values, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT JSON_ARRAY_INSERT(?, %s)", values),
            st -> st.setString(1, json),
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}