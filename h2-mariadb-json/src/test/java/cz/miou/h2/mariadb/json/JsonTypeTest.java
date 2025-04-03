package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonTypeTest {
    
    @ParameterizedTest
    @CsvSource({
        "'[1, 2, {\"key\": \"value\"}]', 'ARRAY'",
        "'{\"key\":\"value\"}', 'OBJECT'",
        "'true', 'BOOLEAN'",
        "'false', 'BOOLEAN'",
        "'1.2', 'DOUBLE'",
        "'1', 'INTEGER'",
        "'null', 'NULL'",
        "'\"a sample string\"', 'STRING'",
        "'invalid', "
    })
    void testJsonType(String json, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_TYPE(?)",
            st -> st.setString(1, json),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}