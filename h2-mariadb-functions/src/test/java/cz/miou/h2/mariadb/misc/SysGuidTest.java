package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class SysGuidTest {
    
    @Test
    void testSysGuid() throws SQLException {
        verifyQuery(
            "SELECT SYS_GUID()",
            rs -> assertThat(rs.getString(1))
                .hasSize(32)
                .containsPattern("^[A-Z0-9]{32}$")
        );
    }
    
}