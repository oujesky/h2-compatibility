package cz.miou.h2.mariadb.time;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.regex.Pattern;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class UtcTimeTest {

    private static final Pattern TIME_PATTERN = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
    private static final Pattern TIME_WITH_PRECISION_PATTERN = Pattern.compile("\\d{2}:\\d{2}:\\d{2}\\.\\d{1,5}");

    @Test
    void testUtcTime() throws SQLException {
        verifyQuery("SELECT UTC_TIME()", rs -> assertThat(rs.getString(1)).matches(TIME_PATTERN));
    }

    @Test
    void testUtcTimeWithPrecision() throws SQLException {
        verifyQuery("SELECT UTC_TIME(5)", rs -> assertThat(rs.getString(1)).matches(TIME_WITH_PRECISION_PATTERN));
    }

}