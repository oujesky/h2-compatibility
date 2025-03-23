package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class DateAddTest {

    @ParameterizedTest
    @CsvSource({
        "'SECOND', '2001-01-01 00:00:00'",
        "'MINUTE', '2001-01-01 00:00:59'",
        "'HOUR', '2001-01-01 00:59:59'",
        "'DAY', '2001-01-01 23:59:59'",
        "'MONTH', '2001-01-31 23:59:59'",
        "'YEAR', '2001-12-31 23:59:59'",
    })
    void testDateAdd(String interval, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT DATE_ADD('2000-12-31 23:59:59', INTERVAL '1' %s)", interval),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }


}