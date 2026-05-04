package cz.miou.h2.mariadb.spatial.function.geohash;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Point;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ERROR_INVALID_GIS_DATA;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_pointfromgeohash">ST_PointFromGeoHash</a>
 */
public class StPointFromGeoHash implements FunctionDefinition {

    private static final String OPERATION_NAME = "ST_PointFromGeoHash";

    @Override
    public String getName() {
        return OPERATION_NAME;
    }

    @Override
    public String getMethodName() {
        return "pointFromGeoHash";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Point pointFromGeoHash(Value geohash, Value srid) {
        if (geohash.containsNull() || srid.containsNull()) {
            return null;
        }

        return GeoHashDecoder.decode(geohash.getString(), validateSrid(srid), OPERATION_NAME);
    }

    private static int validateSrid(Value value) {
        try {
            return value.getInt();
        } catch (Exception e) {
            throw new IllegalArgumentException(ERROR_INVALID_GIS_DATA.apply(value.getType().getDeclaredTypeName(), OPERATION_NAME));
        }
    }

}
