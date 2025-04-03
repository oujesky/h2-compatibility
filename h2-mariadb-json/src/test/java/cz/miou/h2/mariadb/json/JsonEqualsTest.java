package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonEqualsTest {

    @ParameterizedTest
    @CsvSource({
        "'{\"a\"   :[1, 2, 3],\"b\":[4]}', '{\"b\":[4],\"a\":[1, 2, 3.0]}', true",
        "'{\"a\":[1, 2, 3]}', '{\"a\":[1, 2, 3.01]}', false"
    })
    void testJsonEquals(String json1, String json2, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_EQUALS(?, ?)",
            st -> {
                st.setString(1, json1);
                st.setString(2, json2);
            },
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }

}