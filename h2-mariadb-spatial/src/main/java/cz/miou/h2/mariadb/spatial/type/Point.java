package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.api.TypeDefinition;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/point">POINT</a>
 */
public class Point implements TypeDefinition {

    @Override
    public String getName() {
        return "POINT";
    }

    @Override
    public String getType() {
        return "GEOMETRY(POINT)";
    }

}
