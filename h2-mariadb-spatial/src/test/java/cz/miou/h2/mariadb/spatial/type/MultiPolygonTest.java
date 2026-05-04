package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.test.H2ConnectionExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;

import java.sql.Connection;
import java.sql.SQLException;

import static cz.miou.h2.mariadb.spatial.type.GeometryTestUtil.verifyGeometry;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.type.MultiPolygon}.
 */
@ExtendWith(H2ConnectionExtension.class)
class MultiPolygonTest {

    @Test
    void testType(Connection connection) throws SQLException {
        verifyGeometry(connection, "MULTIPOLYGON", "MultiPolygon(((28 26,28 0,84 0,84 42,28 26),(52 18,66 23,73 9,48 6,52 18)),((59 18,67 18,67 13,59 13,59 18)))", MultiPolygon.class, result -> {
            assertThat(result.getNumGeometries())
                .isEqualTo(2);

            assertThat(result.getGeometryN(0).getCoordinates())
                .containsExactly(
                    new Coordinate(28, 26),
                    new Coordinate(28, 0),
                    new Coordinate(84, 0),
                    new Coordinate(84, 42),
                    new Coordinate(28, 26),
                    new Coordinate(52, 18),
                    new Coordinate(66, 23),
                    new Coordinate(73, 9),
                    new Coordinate(48, 6),
                    new Coordinate(52, 18)
                );

            assertThat(result.getGeometryN(1).getCoordinates())
                .containsExactly(
                    new Coordinate(59, 18),
                    new Coordinate(67, 18),
                    new Coordinate(67, 13),
                    new Coordinate(59, 13),
                    new Coordinate(59, 18)
                );
        });
    }

}