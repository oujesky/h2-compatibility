package cz.miou.h2.mariadb.string;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

class FieldTest {

    @ParameterizedTest
    @CsvSource({
        "'Hej', 1",
        "'hej', 1",
        "'HEJ', 1",
        "'ej', 2",
        "'heja', 3",
        "'foo', 5",
        "'fo', 0",
    })
    void testField(String pattern, int expected) throws SQLException {
        verifyQuery(
            "SELECT FIELD(?, 'Hej', 'ej', 'Heja', 'hej', 'foo')",
            st -> st.setString(1, pattern),
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0",
        "1, 5",
        "2, 1",
        "3, 2",
        "4, 3",
        "5, 4",
        "6, 0",
    })
    void testFieldNumbers(int pattern, int expected) throws SQLException {
        verifyQuery(
            "SELECT FIELD(?, 2, 3, 4, 5, 1)",
            st -> st.setInt(1, pattern),
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

    @Test
    void testFieldError() {
        verifyQueryError("SELECT FIELD(1)", "Incorrect parameter count");
    }
}
