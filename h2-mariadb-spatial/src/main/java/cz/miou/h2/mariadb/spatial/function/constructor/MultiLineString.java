package cz.miou.h2.mariadb.spatial.function.constructor;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.LineString;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ensureGeometries;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multilinestring">MULTILINESTRING</a>
 */
public class MultiLineString implements FunctionDefinition {

    @Override
    public String getName() {
        return "MULTILINESTRING";
    }

    @Override
    public String getMethodName() {
        return "multiLineString";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static org.locationtech.jts.geom.MultiLineString multiLineString(Value... lineStrings) {
        if (lineStrings == null || lineStrings.length == 0) {
            return null;
        }

        var geometries = ensureGeometries("multilinestring", LineString.class, lineStrings);
        if (geometries == null) {
            return null;
        }

        return geometries[0].getFactory().createMultiLineString(geometries);
    }
}
