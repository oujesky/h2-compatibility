package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonNormalizeTest {
    
    @ParameterizedTest
    @CsvSource({
        "'{\"name\": \"alice\", \"color\": \"blue\"}', '{\"color\":\"blue\",\"name\":\"alice\"}'",
        "'{\"color\": \"blue\", \"name\": \"alice\"}', '{\"color\":\"blue\",\"name\":\"alice\"}'",
        "'[{\"name\": \"alice\", \"color\": \"blue\"}, {\"color\": \"green\", \"name\": \"alice\"}]', '[{\"color\":\"blue\",\"name\":\"alice\"},{\"color\":\"green\",\"name\":\"alice\"}]'",
        "'{\"b\": {\"name\": \"alice\", \"color\": \"blue\"}, \"a\": {\"name\": \"alice\", \"color\": \"green\"}}', '{\"a\":{\"color\":\"green\",\"name\":\"alice\"},\"b\":{\"color\":\"blue\",\"name\":\"alice\"}}'",
    })
    void testJsonNormalize(String json, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_NORMALIZE(?)",
            st -> st.setString(1, json),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}