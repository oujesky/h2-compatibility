package cz.miou.h2.mariadb.spatial.function.geohash;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.message.DbException;
import org.h2.value.Value;
import org.locationtech.jts.geom.Point;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ERROR_INVALID_GIS_DATA;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.ensureGeometry;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.validateLatitude;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.validateLongitude;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_geohash">ST_GeoHash</a>
 * <p>
 * Available in MariaDB 12+.
 */
public class StGeoHash implements FunctionDefinition {

    private static final int MAX_GEOHASH_LENGTH = 100;

    private static final String OPERATION_NAME = "ST_GeoHash";
    private static final String ERROR_LENGTH_OUT_OF_RANGE = "Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.";

    @Override
    public String getName() {
        return StGeoHash.OPERATION_NAME;
    }

    @Override
    public String getMethodName() {
        return "geoHash";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static String geoHash(Value geometry, Value maxLength) {
        if (geometry.containsNull() || maxLength.containsNull()) {
            return null;
        }

        var point = validatePoint(geometry);

        return geoHash(point.getX(), point.getY(), validateLength(maxLength));
    }

    @SuppressWarnings("unused")
    public static String geoHash(Value longitude, Value latitude, Value maxLength) {
        if (longitude.containsNull() || latitude.containsNull() || maxLength.containsNull()) {
            return null;
        }

        return geoHash(validateCoordinate(longitude), validateCoordinate(latitude), validateLength(maxLength));
    }

    private static String geoHash(double longitude, double latitude, int maxLength) {
        validateLongitude(longitude, OPERATION_NAME);
        validateLatitude(latitude, OPERATION_NAME);
        return GeoHashEncoder.encode(longitude, latitude, maxLength);
    }

    private static Point validatePoint(Value value) {
        var point = ensureGeometry(OPERATION_NAME, Point.class, value, ERROR_INVALID_GIS_DATA);
        if (point == null) {
            throw new IllegalArgumentException(ERROR_INVALID_GIS_DATA.apply(value.getType().getDeclaredTypeName(), OPERATION_NAME));
        }
        return point;
    }

    private static double validateCoordinate(Value value) {
        try {
            return value.getDouble();
        } catch (DbException e) {
            throw new IllegalArgumentException(ERROR_INVALID_GIS_DATA.apply(value.getType().getDeclaredTypeName(), OPERATION_NAME));
        }
    }

    private static int validateLength(Value value) {
        int length;

        try {
            length = value.getInt();
        }  catch (DbException e) {
            throw new IllegalArgumentException(ERROR_LENGTH_OUT_OF_RANGE);
        }

        if (length < 1 || length > MAX_GEOHASH_LENGTH || value.getDouble() != value.getLong()) {
            throw new IllegalArgumentException(ERROR_LENGTH_OUT_OF_RANGE);
        }

        return length;
    }

}
