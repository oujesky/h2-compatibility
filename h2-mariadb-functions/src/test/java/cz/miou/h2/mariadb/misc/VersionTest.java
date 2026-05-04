package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.api.Test;
import org.h2.engine.Constants;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.misc.Version}.
 */
class VersionTest {

    @Test
    void testVersion() throws SQLException {
        verifyQuery(
            "SELECT VERSION()",
            rs -> assertThat(rs.getString(1)).isEqualTo(Constants.VERSION + "-H2")
        );
    }
}
