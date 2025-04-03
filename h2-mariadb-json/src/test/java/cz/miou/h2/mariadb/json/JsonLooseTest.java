package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonLooseTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{ \"A\":1,\"B\":[2,3]}', '{\"A\": 1, \"B\": [2, 3]}'",
        "'{ }', '{}'",
        "'[ ]', '[]'",
    })
    void testJsonLoose(String json, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_LOOSE(?)",
            st -> st.setString(1, json),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}