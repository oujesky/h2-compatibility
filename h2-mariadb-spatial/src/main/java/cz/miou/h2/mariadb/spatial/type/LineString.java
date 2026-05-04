package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.api.TypeDefinition;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/linestring">LINESTRING</a>
 */
public class LineString implements TypeDefinition {

    @Override
    public String getName() {
        return "LINESTRING";
    }

    @Override
    public String getType() {
        return "GEOMETRY(LINESTRING)";
    }

}
