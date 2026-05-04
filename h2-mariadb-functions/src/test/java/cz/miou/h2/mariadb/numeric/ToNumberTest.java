package cz.miou.h2.mariadb.numeric;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.numeric.ToNumber}.
 */
class ToNumberTest {

    @ParameterizedTest
    @CsvSource(value = {
        "123|123",
        "123.45|123.45",
        "'123'|123",
        "'123.45'|123.45",
        "''|NULL",
        "'1E+400'|NULL",
        "'123x'|NULL"
    }, delimiter = '|', quoteCharacter = '"', nullValues = "NULL")
    void testToNumber(String expression, String expected) throws SQLException {
        verifyQuery(
            "SELECT TO_NUMBER(" + expression + ")",
            rs -> assertResult(rs.getObject(1), expected)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "12|00|12",
        "1|999|1",
        ".1|.9999|0.1",
        "123,456.123456|999,999.999999|123456.123456",
        "$12|$99|12",
        "$1|B$9|1",
        "12-|99S|-12",
        "12-|99MI|-12",
        "<12>|99PR|-12",
        "1,2|FM9,9|12",
        "1.2E+1|9.9EEEE|12",
        "FF|FMXXXX|255"
    }, delimiter = '|')
    void testToNumberWithFormat(String input, String format, String expected) throws SQLException {
        verifyQuery(
            "SELECT TO_NUMBER(?, ?)",
            statement -> {
                statement.setString(1, input);
                statement.setString(2, format);
            },
            rs -> assertResult(rs.getObject(1), expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "9G9, G",
        "9D9, D",
        "C9, C",
        "L9, L",
        "U9, U",
        "9V9, V",
        "TM, TM",
        "RN, RN"
    })
    void testToNumberUnsupportedFormat(String format, String unsupportedToken) {
        verifyQueryError(
            "SELECT TO_NUMBER('1', '" + format + "')",
            "Unsupported TO_NUMBER format token: " + unsupportedToken
        );
    }

    private static void assertResult(Object result, String expected) {
        if (expected == null) {
            assertThat(result).isNull();
            return;
        }

        assertThat(((Number) result).doubleValue()).isEqualTo(Double.parseDouble(expected));
    }
}
