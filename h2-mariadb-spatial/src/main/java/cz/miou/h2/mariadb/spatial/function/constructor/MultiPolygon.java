package cz.miou.h2.mariadb.spatial.function.constructor;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Polygon;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ensureGeometries;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multipolygon">MULTIPOLYGON</a>
 */
public class MultiPolygon implements FunctionDefinition {

    @Override
    public String getName() {
        return "MULTIPOLYGON";
    }

    @Override
    public String getMethodName() {
        return "multiPolygon";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static org.locationtech.jts.geom.MultiPolygon multiPolygon(Value... polygons) {
        if (polygons == null || polygons.length == 0) {
            return null;
        }

        var geometries = ensureGeometries("multipolygon", Polygon.class, polygons);
        if (geometries == null) {
            return null;
        }

        return geometries[0].getFactory().createMultiPolygon(geometries);
    }
}
