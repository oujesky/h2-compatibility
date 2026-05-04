package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.test.H2ConnectionExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

import java.sql.Connection;
import java.sql.SQLException;

import static cz.miou.h2.mariadb.spatial.type.GeometryTestUtil.verifyGeometry;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.type.LineString}.
 */
@ExtendWith(H2ConnectionExtension.class)
class LineStringTest {

    @Test
    void testType(Connection connection) throws SQLException {
        verifyGeometry(connection,
            "LINESTRING",
            "LineString(1 1,2 2,3 3)",
            LineString.class,
            result -> assertThat(result.getCoordinates())
                .containsExactly(new Coordinate(1, 1),  new Coordinate(2, 2), new Coordinate(3, 3)));
    }

}