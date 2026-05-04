package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.api.TypeDefinition;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/geometry-constructors/multipoint">MULTIPOINT</a>
 */
public class MultiPoint implements TypeDefinition {

    @Override
    public String getName() {
        return "MULTIPOINT";
    }

    @Override
    public String getType() {
        return "GEOMETRY(MULTIPOINT)";
    }

}
