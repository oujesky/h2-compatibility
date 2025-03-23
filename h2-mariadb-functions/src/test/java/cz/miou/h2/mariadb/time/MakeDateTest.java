package cz.miou.h2.mariadb.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class MakeDateTest {

    @ParameterizedTest
    @CsvSource({
        "2011, 31, '2011-01-31'",
        "2011, 32, '2011-02-01'",
        "2011, 365, '2011-12-31'",
        "2012, 365, '2012-12-30'",
        "2012, 366, '2012-12-31'",
        "2014, 365, '2014-12-31'",
    })
    void testMakeDate(int year, int dayOfYear, String expected) throws SQLException {
        verifyQuery(
            "SELECT MAKEDATE(?, ?)",
            st -> {
                st.setInt(1, year);
                st.setInt(2, dayOfYear);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @Test
    void testMakeDateInvalid() throws SQLException {
        verifyQuery("SELECT MAKEDATE(2011, 0)", rs -> assertThat(rs.getString(1)).isNull());
    }

}