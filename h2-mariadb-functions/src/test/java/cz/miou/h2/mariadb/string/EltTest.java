package cz.miou.h2.mariadb.string;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

class EltTest {

    @ParameterizedTest
    @CsvSource({
        "1, 'ej'",
        "2, 'Heja'",
        "3, 'hej'",
        "4, 'foo'",
    })
    void testElt(int n, String expected) throws SQLException {
        verifyQuery(
            "SELECT ELT(?, 'ej', 'Heja', 'hej', 'foo')",
            st -> st.setInt(1, n),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 5, 10})
    void testEltInvalid(int n) throws SQLException {
        verifyQuery(
            "SELECT ELT(?, 'ej', 'Heja', 'hej', 'foo')",
            st -> st.setInt(1, n),
            rs -> assertThat(rs.getString(1)).isNull()
        );
    }

    @Test
    void testEltError() {
        verifyQueryError("SELECT ELT(1)", "Incorrect parameter count");
    }
}
