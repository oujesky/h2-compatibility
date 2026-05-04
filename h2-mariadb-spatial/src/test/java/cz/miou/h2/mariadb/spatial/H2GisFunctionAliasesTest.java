package cz.miou.h2.mariadb.spatial;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyFunctionExists;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.H2GisFunctionAliases}.
 */
class H2GisFunctionAliasesTest {

    @ParameterizedTest
    @CsvSource({
        // Geometry constructors / operators
        "BUFFER",
        "CONVEXHULL",
        "POINT",
        "PointOnSurface",

        // Geometry properties
        "BOUNDARY",
        "DIMENSION",
//        "ENVELOPE", // not implemented, see H2GisFunctionAliases.Envelope
        "GeometryN",
        "GeometryType",
        "IsClosed",
        "IsEmpty",
        "IsRing",
        "IsSimple",
        "NumGeometries",
        "SRID",

        // Geometry relations
        "CONTAINS",
        "CROSSES",
        "DISJOINT",
        "EQUALS",
        "INTERSECTS",
        "OVERLAPS",
        "TOUCHES",
        "WITHIN",

        // LineString properties
        "ENDPOINT",
        "GLENGTH",
        "NumPoints",
        "PointN",
        "STARTPOINT",

        // Point properties
        "X",
        "Y",

        // Polygon properties
        "AREA",
        "CENTROID",
        "EXTERIORRING",
        "INTERIORRINGN",
        "NumInteriorRings",

        // WKT aliases
        "AsText",
        "AsWKT",
        "GeomCollFromText",
        "GeomFromText",
        "GeometryCollectionFromText",
        "GeometryFromText",
        "LineFromText",
        "LineStringFromText",
        "MLineFromText",
        "MPointFromText",
        "MPolyFromText",
        "MultiLineStringFromText",
        "MultiPointFromText",
        "MultiPolygonFromText",
        "PointFromText",
        "PolyFromText",
        "PolygonFromText",
        "ST_GeomCollFromText",
        "ST_GeometryCollectionFromText",
        "ST_GeometryFromText",
        "ST_LineStringFromText",
        "ST_MultiLineStringFromText",
        "ST_MultiPointFromText",
        "ST_MultiPolygonFromText",
        "ST_PolygonFromText",

        // WKB aliases
        "AsBinary",
        "AsWKB",
        "GeomCollFromWKB",
        "GeomFromWKB",
        "GeometryCollectionFromWKB",
        "GeometryFromWKB",
        "LineFromWKB",
        "LineStringFromWKB",
        "MLineFromWKB",
        "MPointFromWKB",
        "MPolyFromWKB",
        "MultiLineStringFromWKB",
        "MultiPointFromWKB",
        "MultiPolygonFromWKB",
        "PointFromWKB",
        "PolyFromWKB",
        "PolygonFromWKB",
        "ST_AsWKB",
        "ST_GeomCollFromWKB",
        "ST_GeometryCollectionFromWKB",
        "ST_GeometryFromWKB",
        "ST_LineStringFromWKB",
        "ST_MLineFromWKB",
        "ST_MPointFromWKB",
        "ST_MPolyFromWKB",
        "ST_MultiLineStringFromWKB",
        "ST_MultiPointFromWKB",
        "ST_MultiPolygonFromWKB",
        "ST_PolygonFromWKB"
    })
    void testMariadbFunctionAliasesToH2gisFunctions(String function) throws SQLException {
        verifyFunctionExists(function);
    }

}
