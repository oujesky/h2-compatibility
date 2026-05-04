package cz.miou.h2.mariadb.spatial.function.mbr;

import cz.miou.h2.api.FunctionAlias;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/mbr-minimum-bounding-rectangle/mbrequal">MBREquals</a>
 */
public class MbrEquals implements FunctionAlias {

    @Override
    public String getName() {
        return "MBREquals";
    }

    @Override
    public String getClassName() {
        return "cz.miou.h2.mariadb.spatial.function.mbr.MbrEqual";
    }

    @Override
    public String getMethodName() {
        return "mbrEqual";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
}
