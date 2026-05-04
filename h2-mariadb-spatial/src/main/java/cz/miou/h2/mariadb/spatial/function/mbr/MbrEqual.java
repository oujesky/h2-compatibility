package cz.miou.h2.mariadb.spatial.function.mbr;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Envelope;

import static cz.miou.h2.mariadb.spatial.function.mbr.MbrUtil.compareMbr;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrequal">MBREqual</a>
 */
public class MbrEqual implements FunctionDefinition {

    @Override
    public String getName() {
        return "MBREqual";
    }

    @Override
    public String getMethodName() {
        return "mbrEqual";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Boolean mbrEqual(Value g1, Value g2) {
        return compareMbr("mbrequal", g1, g2, Envelope::equals).orElse(null);
    }
}
