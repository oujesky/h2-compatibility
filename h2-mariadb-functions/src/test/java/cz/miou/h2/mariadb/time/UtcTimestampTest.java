package cz.miou.h2.mariadb.time;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.regex.Pattern;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class UtcTimestampTest {

    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    private static final Pattern TIMESTAMP_WITH_PRECISION_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{1,5}");

    @Test
    void testUtcTimestamp() throws SQLException {
        verifyQuery(
            "SELECT UTC_TIMESTAMP()",
            rs -> assertThat(rs.getString(1)).matches(TIMESTAMP_PATTERN));
    }

    @Test
    void testUtcTimestampWithPrecision() throws SQLException {
        verifyQuery(
            "SELECT UTC_TIMESTAMP(5)",
            rs -> assertThat(rs.getString(1)).matches(TIMESTAMP_WITH_PRECISION_PATTERN));
    }

}