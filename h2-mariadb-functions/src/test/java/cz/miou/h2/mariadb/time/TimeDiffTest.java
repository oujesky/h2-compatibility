package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class TimeDiffTest {

    @ParameterizedTest
    @CsvSource({
        "'2000-01-01 00:00:00', '2000-01-01 00:00:00', '00:00:00'",
        "'2000-01-01 00:00:00', '2000-01-01 00:00:00.000001', '-00:00:00.000001'",
        "'2008-12-31 23:59:59.000001', '2008-12-30 01:01:01.000002', '46:58:57.999999'",
        "'23:59:59.000001', '01:01:01.000002', '22:58:57.999999'",
        "'23:59:59', '01:01:01', '22:58:58'"
    })
    void testTimeDiff(String expr1, String expr2, String expected) throws SQLException {
        verifyQuery(
            "SELECT TIMEDIFF(?, ?)",
            st -> {
                st.setString(1, expr1);
                st.setString(2, expr2);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}