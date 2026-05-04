package cz.miou.h2.mariadb.spatial.function.constructor;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2gis.functions.spatial.create.ST_MakePolygon;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;

import java.util.Arrays;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ensureGeometries;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/polygon">POLYGON</a>
 */
public class Polygon implements FunctionDefinition {

    @Override
    public String getName() {
        return "POLYGON";
    }

    @Override
    public String getMethodName() {
        return "polygon";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static org.locationtech.jts.geom.Polygon polygon(Value... values) {
        if (values == null || values.length == 0) {
            return null;
        }

        var lineStrings = ensureGeometries("polygon", LineString.class, values);
        if (lineStrings == null || !allLinearRings(lineStrings)) {
            return null;
        }

        var shell = lineStrings[0];

        if (values.length == 1) {
            return ST_MakePolygon.makePolygon(shell);
        }

        var holes = Arrays.copyOfRange(lineStrings, 1, values.length);

        return ST_MakePolygon.makePolygon(shell, shell.getFactory().createGeometryCollection(holes));
    }

    private static boolean allLinearRings(LineString[] lineStrings) {
        return Arrays.stream(lineStrings)
            .allMatch(lineString -> lineString instanceof LinearRing || lineString.isClosed());
    }

}
