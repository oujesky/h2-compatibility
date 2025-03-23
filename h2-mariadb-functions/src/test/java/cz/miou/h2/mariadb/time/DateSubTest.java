package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class DateSubTest {

    @ParameterizedTest
    @CsvSource({
        "'SECOND', '2024-12-31 23:59:59'",
        "'MINUTE', '2024-12-31 23:59:00'",
        "'HOUR', '2024-12-31 23:00:00'",
        "'DAY', '2024-12-31 00:00:00'",
        "'MONTH', '2024-12-01 00:00:00'",
        "'YEAR', '2024-01-01 00:00:00'",
    })
    void testDateSub(String interval, String expected) throws SQLException {
        verifyQuery(
            String.format("SELECT DATE_SUB('2025-01-01 00:00:00', INTERVAL '1' %s)", interval),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }


}