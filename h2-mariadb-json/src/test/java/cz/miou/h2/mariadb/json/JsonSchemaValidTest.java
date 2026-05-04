package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.json.JsonSchemaValid}.
 */
class JsonSchemaValidTest {

    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "|{\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"}},\"required\":[\"name\"]}|, |{\"name\":\"Ada\"}|, true",
        "|{\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"}},\"required\":[\"name\"]}|, |{\"name\":1}|, false",
        "|{\"type\":\"array\",\"items\":{\"type\":\"integer\"}}|, |[1, 2, 3]|, true",
        "|{\"type\":\"array\",\"items\":{\"type\":\"integer\"}}|, |[1, \"2\"]|, false",
    })
    void testJsonSchemaValid(String schema, String document, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_SCHEMA_VALID(?, ?)",
            st -> {
                st.setString(1, schema);
                st.setString(2, document);
            },
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "|{\"type\":\"string\"}|, ",
        ", |\"value\"|",
    })
    void testJsonSchemaValidNull(String schema, String document) throws SQLException {
        verifyQuery(
            "SELECT JSON_SCHEMA_VALID(?, ?)",
            st -> {
                st.setString(1, schema);
                st.setString(2, document);
            },
            rs -> assertThat(rs.getObject(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "|{\"type\":\"nope\"}|, |\"value\"|",
        "|{\"type\":\"string\"}|, |not json|",
    })
    void testJsonSchemaValidInvalidInput(String schema, String document) throws SQLException {
        verifyQuery(
            "SELECT JSON_SCHEMA_VALID(?, ?)",
            st -> {
                st.setString(1, schema);
                st.setString(2, document);
            },
            rs -> assertThat(rs.getBoolean(1)).isFalse()
        );
    }
}
