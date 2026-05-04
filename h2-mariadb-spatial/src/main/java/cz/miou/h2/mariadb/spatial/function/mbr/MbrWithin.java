package cz.miou.h2.mariadb.spatial.function.mbr;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Envelope;

import static cz.miou.h2.mariadb.spatial.function.mbr.MbrUtil.compareMbr;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrwithin">MBRWithin</a>
 */
public class MbrWithin implements FunctionDefinition {

    @Override
    public String getName() {
        return "MBRWithin";
    }

    @Override
    public String getMethodName() {
        return "mbrWithin";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Boolean mbrWithin(Value g1, Value g2) {
        return compareMbr("mbrwithin", g2, g1, Envelope::contains).orElse(null);
    }
}
