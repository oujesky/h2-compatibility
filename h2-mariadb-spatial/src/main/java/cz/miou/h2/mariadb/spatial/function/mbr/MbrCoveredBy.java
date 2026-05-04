package cz.miou.h2.mariadb.spatial.function.mbr;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.value.Value;
import org.locationtech.jts.geom.Envelope;

import static cz.miou.h2.mariadb.spatial.function.mbr.MbrUtil.compareMbr;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrcoveredby">MBRCoveredBy</a>
 */
public class MbrCoveredBy implements FunctionDefinition {

    @Override
    public String getName() {
        return "MBRCoveredBy";
    }

    @Override
    public String getMethodName() {
        return "mbrCoveredBy";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Boolean mbrCoveredBy(Value g1, Value g2) {
        return compareMbr("mbrcoveredby", g2, g1, Envelope::covers).orElse(null);

    }
}
