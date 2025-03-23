package cz.miou.h2.mariadb.time;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class MakeTimeTest {

    @ParameterizedTest
    @CsvSource({
        "13, 57, 33, '13:57:33'",
        "0, 0, 0, '00:00:00'",
        "23, 59, 59, '23:59:59'",
    })
    void testMakeTime(int hour, int minute, int second, String expected) throws SQLException {
        verifyQuery(
            "SELECT MAKETIME(?, ?, ?)",
            st -> {
                st.setInt(1, hour);
                st.setInt(2, minute);
                st.setInt(3, second);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @Test
    void testMakeDateInvalid() throws SQLException {
        verifyQuery("SELECT MAKETIME(13, 67, 33)", rs -> assertThat(rs.getString(1)).isNull());
    }
}