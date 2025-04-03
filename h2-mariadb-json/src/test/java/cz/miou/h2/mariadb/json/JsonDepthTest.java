package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonDepthTest {
    
    @ParameterizedTest
    @CsvSource({
        "'[]', 1",
        "'true', 1",
        "'{}', 1",
        "'[1, 2, 3]', 2",
        "'[[], {}, []]', 2",
        "'[1, 2, [3, 4, 5, 6], 7]', 3"
    })
    void testJsonDepth(String json, int expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_DEPTH(?)",
            st -> st.setString(1, json),
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }
    
}