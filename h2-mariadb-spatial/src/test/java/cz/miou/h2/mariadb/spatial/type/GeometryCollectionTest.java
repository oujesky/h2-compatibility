package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.test.H2ConnectionExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.LineString;

import java.sql.Connection;
import java.sql.SQLException;

import static cz.miou.h2.mariadb.spatial.type.GeometryTestUtil.verifyGeometry;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.type.GeometryCollection}.
 */
@ExtendWith(H2ConnectionExtension.class)
class GeometryCollectionTest {

    @SuppressWarnings("unchecked")
    @Test
    void testType(Connection connection) throws SQLException {
        verifyGeometry(connection, "GEOMETRYCOLLECTION", "GeometryCollection(Point(0 0), LineString(0 0,10 10))", GeometryCollection.class, result -> {
            assertThat(result.getNumGeometries())
                .isEqualTo(2);
            assertThat(result.getGeometryN(0))
                .isInstanceOf(Point.class);
            assertThat(result.getGeometryN(1))
                .isInstanceOf(LineString.class);
        });
    }

}