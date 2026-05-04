package cz.miou.h2.mariadb.spatial.function.mbr;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Envelope;

import static cz.miou.h2.mariadb.spatial.function.mbr.MbrUtil.compareMbr;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrintersects">MBRIntersects</a>
 */
public class MbrIntersects implements FunctionDefinition {

    @Override
    public String getName() {
        return "MBRIntersects";
    }

    @Override
    public String getMethodName() {
        return "mbrIntersects";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Boolean mbrIntersects(Value g1, Value g2) {
        return compareMbr("mbrintersects", g1, g2, Envelope::intersects).orElse(null);

    }
}
