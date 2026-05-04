package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.api.TypeDefinition;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multipolygon">MULTIPOLYGON</a>
 */
public class MultiPolygon implements TypeDefinition {

    @Override
    public String getName() {
        return "MULTIPOLYGON";
    }

    @Override
    public String getType() {
        return "GEOMETRY(MULTIPOLYGON)";
    }

}
