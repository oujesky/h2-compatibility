package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonCompactTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{ \"A\": 1, \"B\": [2, 3] }', '{\"A\":1,\"B\":[2,3]}'",
        "'{\"A\":1,\"B\":[2,3]}', '{\"A\":1,\"B\":[2,3]}'",
    })
    void testJsonCompact(String json, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_COMPACT(?)",
            st -> st.setString(1, json),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}