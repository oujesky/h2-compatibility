package cz.miou.h2.mariadb.spatial.function.constructor;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ensureGeometries;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/geometrycollection">GEOMETRYCOLLECTION</a>
 */
public class GeometryCollection implements FunctionDefinition {

    @Override
    public String getName() {
        return "GEOMETRYCOLLECTION";
    }

    @Override
    public String getMethodName() {
        return "geometryCollection";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static org.locationtech.jts.geom.GeometryCollection geometryCollection(Value... values) {
        if (values == null || values.length == 0) {
            return null;
        }

        var geometries = ensureGeometries("geometrycollection", values);

        return geometries[0].getFactory().createGeometryCollection(geometries);
    }
}
