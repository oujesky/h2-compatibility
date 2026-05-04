package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.api.TypeDefinition;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/geometrycollection">GEOMETRYCOLLECTION</a>
 */
public class GeometryCollection implements TypeDefinition {

    @Override
    public String getName() {
        return "GEOMETRYCOLLECTION";
    }

    @Override
    public String getType() {
        return "GEOMETRY(GEOMETRYCOLLECTION)";
    }

}
