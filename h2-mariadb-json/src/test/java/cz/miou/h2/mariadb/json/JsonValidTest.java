package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonValidTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{\"id\": 1, \"name\": \"Monty\"}', true",
        "'{\"id\": 1, \"name\": \"Monty\", \"oddfield\"}', false"
    })
    void testJsonValid(String value, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_VALID(?)",
            st -> st.setString(1, value),
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }
    
}