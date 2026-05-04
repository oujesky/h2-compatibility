package cz.miou.h2.mariadb.spatial;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyFunctionExists;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.H2GisLoader}.
 */
class H2GisLoaderTest {

    @ParameterizedTest
    @CsvSource({
        // Constructors / operators
        "ST_BUFFER",
        "ST_CONVEXHULL",
        "ST_INTERSECTION",
        "ST_POINTONSURFACE",
        "ST_SYMDIFFERENCE",
        "ST_UNION",

        // Geometry properties
        "ST_BOUNDARY",
        "ST_DIMENSION",
        "ST_ENVELOPE",
        "ST_GEOMETRYN",
        "ST_GEOMETRYTYPE",
        "ST_ISCLOSED",
        "ST_ISEMPTY",
        "ST_IsRing",
        "ST_IsSimple",
        "ST_NUMGEOMETRIES",
        "ST_RELATE",
        "ST_SRID",

        // Geometry relations / measurements
        "ST_CONTAINS",
        "ST_CROSSES",
        "ST_DIFFERENCE",
        "ST_DISJOINT",
        "ST_DISTANCE",
        "ST_EQUALS",
        "ST_INTERSECTS",
        "ST_LENGTH",
        "ST_OVERLAPS",
        "ST_TOUCHES",
        "ST_WITHIN",

        // LineString properties
        "ST_ENDPOINT",
        "ST_NUMPOINTS",
        "ST_POINTN",
        "ST_STARTPOINT",

        // Misc GIS
        "ST_Collect",
        "ST_IsValid",
        "ST_Simplify",

        // Point properties
        "ST_X",
        "ST_Y",

        // Polygon properties
        "ST_AREA",
        "ST_CENTROID",
        "ST_EXTERIORRING",
        "ST_INTERIORRINGN",
        "ST_NUMINTERIORRINGS",

        // WKT
        "ST_AsText",
        "ST_AsWKT",
        "ST_GeomFromText",
        "ST_LineFromText",
        "ST_MLineFromText",
        "ST_MPointFromText",
        "ST_MPolyFromText",
        "ST_PointFromText",
        "ST_PolyFromText",

        // WKB
        "ST_AsBinary",
        "ST_GeomFromWKB",
        "ST_LineFromWKB",
        "ST_PointFromWKB",
        "ST_PolyFromWKB"
    })
    void testMariadbFunctionsPresentInH2gis(String function) throws SQLException {
        verifyFunctionExists(function);
    }

}