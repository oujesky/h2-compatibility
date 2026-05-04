package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.misc.ConnectionId}.
 */
class ConnectionIdTest {

    @Test
    void testConnectionIdIsStableForConnection() throws SQLException {
        verifyQuery(
            "SELECT CONNECTION_ID(), CONNECTION_ID() WHERE ? = 1",
            statement -> statement.setInt(1, 1),
            rs -> {
                assertThat(rs.getLong(1)).isPositive();
                assertThat(rs.getLong(1)).isEqualTo(rs.getLong(2));
            }
        );
    }
}
