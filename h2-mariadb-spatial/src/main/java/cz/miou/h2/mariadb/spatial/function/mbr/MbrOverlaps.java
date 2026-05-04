package cz.miou.h2.mariadb.spatial.function.mbr;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Envelope;

import static cz.miou.h2.mariadb.spatial.function.mbr.MbrUtil.compareMbr;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbroverlaps">MBROverlaps</a>
 */
public class MbrOverlaps implements FunctionDefinition {

    @Override
    public String getName() {
        return "MBROverlaps";
    }

    @Override
    public String getMethodName() {
        return "mbrOverlaps";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Boolean mbrOverlaps(Value g1, Value g2) {
        return compareMbr("mbroverlaps", g1, g2, MbrOverlaps::overlaps).orElse(null);
    }

    private static boolean overlaps(Envelope e1, Envelope e2) {
        var dimension = dimension(e1);
        if (dimension != dimension(e2) || dimension <= 0 || e1.contains(e2) || e2.contains(e1)) {
            return false;
        }

        var intersection = e1.intersection(e2);
        return dimension == dimension(intersection);
    }

    private static int dimension(Envelope envelope) {
        if (envelope.isNull()) {
            return -1;
        }

        var dimension = 0;
        if (envelope.getMinX() < envelope.getMaxX()) {
            dimension++;
        }
        if (envelope.getMinY() < envelope.getMaxY()) {
            dimension++;
        }
        return dimension;
    }
}
