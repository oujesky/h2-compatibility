package cz.miou.h2.mariadb.spatial.function.constructor;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Point;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ensureGeometries;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multipoint">MULTIPOINT</a>
 */
public class MultiPoint implements FunctionDefinition {

    @Override
    public String getName() {
        return "MULTIPOINT";
    }

    @Override
    public String getMethodName() {
        return "multiPoint";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static org.locationtech.jts.geom.MultiPoint multiPoint(Value... points) {
        if (points == null || points.length == 0) {
            return null;
        }

        var geometries = ensureGeometries("multipoint", Point.class, points);
        if (geometries == null) {
            return null;
        }

        return geometries[0].getFactory().createMultiPoint(geometries);
    }
}
