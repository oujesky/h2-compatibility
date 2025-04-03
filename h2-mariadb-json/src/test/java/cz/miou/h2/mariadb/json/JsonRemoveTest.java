package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonRemoveTest {
    
    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "|{\"A\": 1, \"B\": 2, \"C\": {\"D\": 3}}|, |'$.C'|, |{\"A\": 1, \"B\": 2}|",
        "|{\"A\": 1, \"B\": 2, \"C\": {\"D\": 3}}|, |'$.C.D'|, |{\"A\": 1, \"B\": 2, \"C\": {}}|",
        "|{\"A\": 1, \"B\": 2, \"C\": {\"D\": 3}}|, |'$.D'|, |{\"A\": 1, \"B\": 2, \"C\": {\"D\": 3}}|",
        "|{\"A\": 1, \"B\": 2, \"C\": {\"D\": 3}}|, |'$.C', '$.C'|, |{\"A\": 1, \"B\": 2}|",
        "|{\"A\": 1, \"B\": 2, \"C\": {\"D\": 3}}|, |'$.A', '$.B', '$.C'|, |{}|",

        "|{\"A\": 1, \"B\": 2, \"C\": {\"D\": 3}}|, |'$'|, ",
        "|{\"A\": 1, \"B\": 2, \"C\": {\"D\": 3}}|, |'$.*'|, ",

        "|[\"A\", \"B\", [\"C\", \"D\"], \"E\"]|, |'$[1]'|, |[\"A\", [\"C\", \"D\"], \"E\"]|",
        "|[\"A\", \"B\", [\"C\", \"D\"], \"E\"]|, |'$[0]', '$[0]', '$[0]', '$[0]', '$[0]'|, |[]|",
        "|[\"A\", \"B\", [\"C\", \"D\"], \"E\"]|, |'$[6]'|, |[\"A\", \"B\", [\"C\", \"D\"], \"E\"]|",
    })
    void testJsonRemove(String json, String paths, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT JSON_REMOVE(?, %s)", paths),
            st -> st.setString(1, json),
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}