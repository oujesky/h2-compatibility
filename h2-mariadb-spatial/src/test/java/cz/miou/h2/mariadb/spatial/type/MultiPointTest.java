package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.test.H2ConnectionExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPoint;

import java.sql.Connection;
import java.sql.SQLException;

import static cz.miou.h2.mariadb.spatial.type.GeometryTestUtil.verifyGeometry;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.type.MultiPoint}.
 */
@ExtendWith(H2ConnectionExtension.class)
class MultiPointTest {

    @Test
    void testType(Connection connection) throws SQLException {
        verifyGeometry(connection,
            "MULTIPOINT",
            "MultiPoint( 1 1, 2 2, 5 3, 7 2, 9 3, 8 4, 6 6, 6 9, 4 9, 1 5 )",
            MultiPoint.class,
            result -> assertThat(result.getCoordinates())
                .containsExactly(
                    new Coordinate(1, 1),
                    new Coordinate(2, 2),
                    new Coordinate(5, 3),
                    new Coordinate(7, 2),
                    new Coordinate(9, 3),
                    new Coordinate(8, 4),
                    new Coordinate(6, 6),
                    new Coordinate(6, 9),
                    new Coordinate(4, 9),
                    new Coordinate(1, 5)
                ));
    }

}