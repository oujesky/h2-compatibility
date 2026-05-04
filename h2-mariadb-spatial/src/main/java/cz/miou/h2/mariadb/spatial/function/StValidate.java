package cz.miou.h2.mariadb.spatial.function;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.h2.value.ValueGeometry;
import org.h2.value.ValueNull;
import org.locationtech.jts.geom.Point;

import static cz.miou.h2.mariadb.spatial.GeometryUtil.ensureGeometry;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.validateLatitude;
import static cz.miou.h2.mariadb.spatial.GeometryUtil.validateLongitude;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_validate">ST_Validate</a>
 */
public class StValidate implements FunctionDefinition {

    private static final String OPERATION_NAME = "ST_Validate";

    @Override
    public String getName() {
        return OPERATION_NAME;
    }

    @Override
    public String getMethodName() {
        return "validate";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Value validate(Value value) {
        if (value instanceof ValueGeometry) {
            var geometry = ensureGeometry(OPERATION_NAME, value);
            if (geometry != null && geometry.isValid()) {
                if (geometry instanceof Point) {
                    var point = (Point) geometry;
                    validateLongitude(point.getX(), OPERATION_NAME);
                    validateLatitude(point.getY(), OPERATION_NAME);
                }

                return value;
            }
        }

        return ValueNull.INSTANCE;
    }
}
