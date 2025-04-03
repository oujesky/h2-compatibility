package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonUnquoteTest {
    
    @ParameterizedTest
    @CsvSource({
        "'\"Monty\"', 'Monty'",
        "'Si\\bng\\ting', 'Sing\ting'",
        "'\"abc\"', 'abc'",
        "'[1, 2, 3]', '[1, 2, 3]'",
        "'\"\\\\t\\\\u0032\"', '\t2'",
//        "'', ''",
//        "'', ''",
//        "'', ''",
    })
    void testJsonUnquote(String val, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_UNQUOTE(?)",
            st -> st.setString(1, val),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}