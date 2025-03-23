package cz.miou.h2.mariadb.time;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.regex.Pattern;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class UtcDateTest {

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    @Test
    void testUtcDate() throws SQLException {
        verifyQuery("SELECT UTC_DATE()", rs -> assertThat(rs.getString(1)).matches(DATE_PATTERN));
    }

}