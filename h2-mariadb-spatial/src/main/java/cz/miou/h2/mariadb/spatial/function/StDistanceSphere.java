package cz.miou.h2.mariadb.spatial.function;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.message.DbException;
import org.h2.value.Value;
import org.h2.value.ValueInteger;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ERROR_INVALID_GIS_DATA;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.ensureGeometry;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.validateLatitude;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.validateLongitude;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-relations/st_distance_sphere">ST_DISTANCE_SPHERE</a>
 */
public class StDistanceSphere implements FunctionDefinition {

    private static final int EARTH_RADIUS = 6370986;

    private static final String OPERATION_NAME = "ST_Distance_Sphere";
    private static final String ERROR_UNSUPPORTED_TYPES_OF_ARGUMENTS = "Calling geometry function st_distance_sphere with unsupported types of arguments.";

    @Override
    public String getName() {
        return "ST_DISTANCE_SPHERE";
    }

    @Override
    public String getMethodName() {
        return "distanceSphere";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Double distanceSphere(Value g1, Value g2) {
        return distanceSphere(g1, g2, ValueInteger.get(EARTH_RADIUS));
    }

    public static Double distanceSphere(Value g1, Value g2, Value r) {
        if (g1.containsNull() || g2.containsNull() || r.containsNull()) {
            return null;
        }

        var coordinates1 = validateGeometry(g1);
        var coordinates2 = validateGeometry(g2);
        var radius = validateRadius(r);

        if (coordinates1.size() == 1 && coordinates2.size() == 1) {
            return haversineDistance(coordinates1.iterator().next(), coordinates2.iterator().next(), radius);
        }

        var coordinates = new HashSet<Set<Coordinate>>();
        for (var c1 : coordinates1) {
            for (var c2 : coordinates2) {
                if (Objects.equals(c1, c2)) {
                    return 0.0;
                }
                coordinates.add(Set.of(c1, c2));
            }
        }

        return coordinates.stream()
            .mapToDouble(pair -> {
                var it = pair.iterator();
                var a = it.next();
                var b = it.next();
                return haversineDistance(a, b, radius);
            })
            .min()
            .orElse(0);
    }


    private static double validateRadius(Value value) {
        double radius;
        try {
            radius = value.getDouble();
        } catch (DbException e) {
            throw new IllegalArgumentException(ERROR_UNSUPPORTED_TYPES_OF_ARGUMENTS);
        }

        if (radius <= 0) {
            throw new IllegalArgumentException(ERROR_UNSUPPORTED_TYPES_OF_ARGUMENTS);
        }

        return radius;
    }

    private static Set<Coordinate> validateGeometry(Value value) {
        var geometry = ensureGeometry(OPERATION_NAME, value, ERROR_INVALID_GIS_DATA);

        if (geometry instanceof Point || geometry instanceof MultiPoint) {
            var coordinates = Set.of(geometry.getCoordinates());
            coordinates.forEach(StDistanceSphere::validateCoordinates);
            return coordinates;
        }

        throw new IllegalArgumentException(ERROR_UNSUPPORTED_TYPES_OF_ARGUMENTS);
    }

    private static void validateCoordinates(Coordinate coordinate) {
        validateLongitude(coordinate.getX(), OPERATION_NAME);
        validateLatitude(coordinate.getY(), OPERATION_NAME);
    }

    private static double haversineDistance(Coordinate p1, Coordinate p2, double radius) {
        if (Objects.equals(p1, p2)) {
            return 0;
        }

        var lon1 = p1.getX();
        var lon2 = p2.getX();
        var lonDist = Math.toRadians(lon2 - lon1);

        var lat1 = p1.getY();
        var lat2 = p2.getY();
        var latDist = Math.toRadians(lat2 - lat1);

        var a = Math.pow(Math.sin(latDist / 2), 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(lonDist / 2), 2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return radius * c;
    }

}
