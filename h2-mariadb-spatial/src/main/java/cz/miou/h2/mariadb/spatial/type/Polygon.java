package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.api.TypeDefinition;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/polygon">POLYGON</a>
 */
public class Polygon implements TypeDefinition {

    @Override
    public String getName() {
        return "POLYGON";
    }

    @Override
    public String getType() {
        return "GEOMETRY(POLYGON)";
    }

}
