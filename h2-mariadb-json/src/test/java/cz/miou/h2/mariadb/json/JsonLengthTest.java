package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class JsonLengthTest {

    @ParameterizedTest
    @CsvSource({
        "'null', '$', 1",
        "'true', '$', 1",
        "'123', '$', 1",
        "'\"abc\"', '$', 1",

        "'[]', '$', 0",
        "'[1, 2, 3]', '$', 3",

        "'{}', '$', 0",
        "'{\"A\": 1, \"B\": 2}', '$', 2",

        "'{\"A\": 1}', '$.A', 1",
        "'{\"A\": [1, 2]}', '$.A', 2",
        "'{\"A\": {\"B\": 123}}', '$.A', 1",

        "'{}', '$.A', ",
        "'[]', '$[0]', ",
    })
    void testJsonLength(String json, String path, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_LENGTH(?, ?)",
            st -> {
                st.setString(1, json);
                st.setString(2, path);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}