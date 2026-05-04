package cz.miou.h2.mariadb.spatial.function.constructor;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import java.util.Arrays;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ensureGeometries;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/linestring">LINESTRING</a>
 * <p>
 * Differences to MariaDB:
 * <ul>
 *   <li>in MariaDB, LineString(Point(...)) with single point on input is legal and results in LINESTRING(0 0)
 *      - underlying {@link org.locationtech.jts.geom.LineString} requires at least 2 points to be present
 * </ul>
 */
public class LineString implements FunctionDefinition {

    @Override
    public String getName() {
        return "LINESTRING";
    }

    @Override
    public String getMethodName() {
        return "lineString";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static org.locationtech.jts.geom.LineString lineString(Value... points) {
        if (points == null || points.length == 0) {
            return null;
        }

        var geometries = ensureGeometries("linestring", Point.class, points);
        if (geometries == null) {
            return null;
        }

        var coordinates = Arrays.stream(geometries)
            .map(Point::getCoordinate)
            .toArray(Coordinate[]::new);

        return geometries[0].getFactory().createLineString(coordinates);
    }
}
