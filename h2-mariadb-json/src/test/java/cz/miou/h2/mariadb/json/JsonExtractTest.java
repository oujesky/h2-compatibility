package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonExtractTest {

    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "|[1, 2, [3, 4]]|, |'$[1]'|, |2|",
        "|[1, 2, [3, 4]]|, |'$[2]'|, |[3, 4]|",
        "|[1, 2, [3, 4]]|, |'$[1]', '$[2]'|, |[2, [3, 4]]|",
        "|[1, 2, [3, 4]]|, |'$[2][1]'|, |4|",

        "|[1, 2, [3, 4]]|, |'$[1]', '$[0]'|, |[2, 1]|",
        "|[1, 2, [3, 4]]|, |'$[1]', '$[6]', '$[0]'|, |[2, 1]|",
        "|[1, 2, [3, 4]]|, |'$[6]', '$[0]'|, |1|",
        "|[1, 2, [3, 4]]|, |'$[0]', '$[6]'|, |1|",

        "|[1, 2, [3, 4]]|, |'$'|, |[1, 2, [3, 4]]|",

        "|[null]|, |'$[0]'|, |null|",
        "|[]|, |'$[0]'|, ",
    })
    void testJsonExtract(String json, String path, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT JSON_EXTRACT(?, %s)", path),
            st -> st.setString(1, json),
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }

}