package cz.miou.h2.mariadb.spatial.function.geohash;

import cz.miou.h2.api.FunctionDefinition;

/**
 * <a href="https://mariadb.com/docs/server/reference/sql-statements/geometry-constructors/miscellaneous-gis-functions/st_latfromgeohash">ST_LatFromGeoHash</a>
 */
public class StLatFromGeoHash implements FunctionDefinition {

    private static final String OPERATION_NAME = "ST_LatFromGeoHash";

    @Override
    public String getName() {
        return OPERATION_NAME;
    }

    @Override
    public String getMethodName() {
        return "latFromGeoHash";
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }

    @SuppressWarnings("unused")
    public static Double latFromGeoHash(String geohash) {
        if (geohash == null) {
            return null;
        }

        return GeoHashDecoder.decode(geohash, 0, OPERATION_NAME).getY();
    }
}
