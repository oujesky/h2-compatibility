package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class RegexpInstrTest {

    @ParameterizedTest
    @CsvSource({
        "'abc','b', 2",
        "'abc', 'x', 0",
        "'BJÖRN', 'N', 5",
        "'BJÖRN', '[a-z]', 0",
        "'BJÖRN', '[A-Z]', 1",
        "'ABC', '(?-i)b', 0",
        "'ABC', '(?i)b', 2",
    })
    void testRegexpInstr(String subject, String pattern, int expected) throws SQLException {
        verifyQuery(
            "SELECT REGEXP_INSTR(?, ?)",
            st -> {
                st.setString(1, subject);
                st.setString(2, pattern);
            },
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

}