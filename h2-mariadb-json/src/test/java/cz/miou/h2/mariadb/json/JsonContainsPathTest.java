package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonContainsPathTest {
    
    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "|{\"A\": 1, \"B\": [2], \"C\": [3, 4]}|, |one|, |'$.A', '$.D'|, true",
        "|{\"A\": 1, \"B\": [2], \"C\": [3, 4]}|, |all|, |'$.A', '$.D'|, false",
    })
    void testJsonContainsPath(String json, String mode, String paths, boolean expected) throws SQLException {
        verifyQuery(
            String.format("SELECT JSON_CONTAINS_PATH(?, ?, %s)", paths),
            st -> {
                st.setString(1, json);
                st.setString(2, mode);
            },
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }
    
}