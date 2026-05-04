package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.api.TypeDefinition;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multilinestring">MULTILINESTRING</a>
 */
public class MultiLineString implements TypeDefinition {

    @Override
    public String getName() {
        return "MULTILINESTRING";
    }

    @Override
    public String getType() {
        return "GEOMETRY(MULTILINESTRING)";
    }

}
