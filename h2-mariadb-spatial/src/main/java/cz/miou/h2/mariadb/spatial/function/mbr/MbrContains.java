package cz.miou.h2.mariadb.spatial.function.mbr;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Envelope;

import static cz.miou.h2.mariadb.spatial.function.mbr.MbrUtil.compareMbr;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrcontains">MBRContains</a>
 */
public class MbrContains implements FunctionDefinition {

    @Override
    public String getName() {
        return "MBRContains";
    }

    @Override
    public String getMethodName() {
        return "mbrContains";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Boolean mbrContains(Value g1, Value g2) {
        return compareMbr("mbrcontains", g1, g2, Envelope::contains).orElse(null);
    }
}
