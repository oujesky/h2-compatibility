package cz.miou.h2.mariadb.spatial.function.geohash;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_longfromgeohash">ST_LongFromGeoHash</a>
 */
public class StLongFromGeoHash implements FunctionDefinition {

    private static final String OPERATION_NAME = "ST_LongFromGeoHash";

    @Override
    public String getName() {
        return OPERATION_NAME;
    }

    @Override
    public String getMethodName() {
        return "lonFromGeoHash";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Double lonFromGeoHash(String geohash) {
        if (geohash == null) {
            return null;
        }

        return GeoHashDecoder.decode(geohash, 0, OPERATION_NAME).getX();
    }
}
