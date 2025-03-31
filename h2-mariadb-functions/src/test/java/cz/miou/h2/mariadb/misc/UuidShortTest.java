package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class UuidShortTest {
    
    @Test
    void testUuidShort() throws SQLException {
        verifyQuery(
            "SELECT UUID_SHORT()",
            rs -> assertThat(rs.getLong(1)).isGreaterThan(0L)
        );
    }
    
}