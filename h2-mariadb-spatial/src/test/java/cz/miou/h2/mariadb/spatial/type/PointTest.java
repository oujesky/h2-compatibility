package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.test.H2ConnectionExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Point;

import java.sql.Connection;
import java.sql.SQLException;

import static cz.miou.h2.mariadb.spatial.type.GeometryTestUtil.verifyGeometry;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.type.Point}.
 */
@ExtendWith(H2ConnectionExtension.class)
class PointTest {

    @Test
    void testType(Connection connection) throws SQLException {
        verifyGeometry(connection, "POINT", "Point(1 10)", Point.class, result -> {
            assertThat(result.getX()).isEqualTo(1);
            assertThat(result.getY()).isEqualTo(10);
        });
    }

}