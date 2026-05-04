package cz.miou.h2.mariadb.spatial;

import org.h2.value.Value;
import org.h2.value.ValueGeometry;
import org.locationtech.jts.geom.Geometry;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.BinaryOperator;

public class GeometryUtil {

    public static final double MAX_LATITUDE = 90.0;
    public static final double MIN_LATITUDE = -90.0;
    public static final double MAX_LONGITUDE = 180.0;
    public static final double MIN_LONGITUDE = -180.0;

    public static final BinaryOperator<String> ERROR_INVALID_GIS_DATA = (type, operation) -> String.format("Invalid GIS data provided to function %s.", operation);
    public static final BinaryOperator<String> ERROR_ILLEGAL_PARAMETER = (type, operation) -> String.format("Illegal parameter data type %s for operation '%s'", type, operation);

    private GeometryUtil() {}

    public static Geometry[] ensureGeometries(String operation, Value... values) {
        return ensureGeometries(operation, Geometry.class, values);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Geometry> T[] ensureGeometries(String operation, Class<T> type, Value... values) {
        try {
            return Arrays.stream(values)
                .map(value -> {
                    var geometry = ensureGeometry(operation, type, value);
                    if (geometry == null) {
                        throw new NullPointerException();
                    }
                    return geometry;
                })
                .toArray(length -> (T[]) Array.newInstance(type, length));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static Geometry ensureGeometry(String operation, Value value) {
        return ensureGeometry(operation, Geometry.class, value);
    }

    public static Geometry ensureGeometry(String operation, Value value, BinaryOperator<String> errorMessage) {
        return ensureGeometry(operation, Geometry.class, value, errorMessage);
    }

    public static <T extends Geometry> T ensureGeometry(String operation, Class<T> type, Value value) {
        return ensureGeometry(operation, type, value, ERROR_ILLEGAL_PARAMETER);
    }

    public static <T extends Geometry> T ensureGeometry(String operation, Class<T> type, Value value, BinaryOperator<String> errorMessage) {
        if (value instanceof ValueGeometry) {
            var geometry = ((ValueGeometry) value).getGeometry();
            if (type.isAssignableFrom(geometry.getClass())) {
                return type.cast(geometry);
            } else {
                return null;
            }
        }

        if (value.containsNull()) {
            throw new IllegalArgumentException("Illegal non geometric 'NULL' value found during parsing");
        }

        throw new IllegalArgumentException(errorMessage.apply(value.getType().getDeclaredTypeName(), operation));
    }

    public static double validateLongitude(double longitude, String operation) {
        return validateRange(longitude, MIN_LONGITUDE, MAX_LONGITUDE, "Longitude", operation);
    }

    public static double validateLatitude(double latitude, String operation) {
        return validateRange(latitude, MIN_LATITUDE, MAX_LATITUDE, "Latitude", operation);
    }

    private static double validateRange(double value, double min, double max, String name, String operation) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(String.format("Out of range error: %s should be [%s,%s] in function %s.", name, formatNumber(min), formatNumber(max), operation));
        }
        return value;
    }

    private static String formatNumber(double value) {
        return value == (long) value ? String.format("%d", (long) value) : String.format("%s", value);
    }

}
