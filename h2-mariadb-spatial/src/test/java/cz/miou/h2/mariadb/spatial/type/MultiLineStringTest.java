package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.test.H2ConnectionExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiLineString;

import java.sql.Connection;
import java.sql.SQLException;

import static cz.miou.h2.mariadb.spatial.type.GeometryTestUtil.verifyGeometry;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.type.MultiLineString}.
 */
@ExtendWith(H2ConnectionExtension.class)
class MultiLineStringTest {

    @Test
    void testType(Connection connection) throws SQLException {
        verifyGeometry(connection, "MULTILINESTRING", "MultiLineString((10 48,10 21,10 0),(16 0,16 23,16 48))", MultiLineString.class, result -> {
            assertThat(result.getNumGeometries())
                .isEqualTo(2);
            assertThat(result.getGeometryN(0).getCoordinates())
                .containsExactly(new Coordinate(10, 48),  new Coordinate(10, 21), new Coordinate(10, 0));
            assertThat(result.getGeometryN(1).getCoordinates())
                .containsExactly(new Coordinate(16, 0),  new Coordinate(16, 23), new Coordinate(16, 48));
        });
    }

}