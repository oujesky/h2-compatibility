package cz.miou.h2.mariadb.spatial.function.mbr;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Envelope;

import static cz.miou.h2.mariadb.spatial.function.mbr.MbrUtil.compareMbr;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrtouches">MBRTouches</a>
 */
public class MbrTouches implements FunctionDefinition {

    @Override
    public String getName() {
        return "MBRTouches";
    }

    @Override
    public String getMethodName() {
        return "mbrTouches";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Boolean mbrTouches(Value g1, Value g2) {
        return compareMbr("mbrtouches", g1, g2, MbrTouches::touches).orElse(null);
    }

    private static Boolean touches(Envelope e1, Envelope e2) {
        return ((e2.getMinX() == e1.getMaxX() || e2.getMaxX() == e1.getMinX())
                && ((e2.getMinY() >= e1.getMinY() && e2.getMinY() <= e1.getMaxY())
                    || (e2.getMaxY() >= e1.getMinY() && e2.getMaxY() <= e1.getMaxY())))
               || ((e2.getMinY() == e1.getMaxY() || e2.getMaxY() == e1.getMinY())
                   && ((e2.getMinX() >= e1.getMinX() && e2.getMinX() <= e1.getMaxX())
                       || (e2.getMaxX() >= e1.getMinX() && e2.getMaxX() <= e1.getMaxX())));
    }
}
