package cz.miou.h2.mariadb.spatial.function.constructor;

import cz.miou.h2.mariadb.spatial.test.WktAggregator;
import cz.miou.h2.mariadb.spatial.test.WktGeometries;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.function.constructor.Polygon}.
 */
class PolygonTest {

    @ParameterizedTest
    @CsvSource({
        "'POLYGON ((0 0, 0 1, 1 1, 0 0))', 'LINESTRING(0 0,0 1,1 1,0 0)'",
        "'POLYGON ((0 0, 0 1, 1 1, 1 0, 0 0))', 'LINESTRING(0 0,0 1,1 1,1 0,0 0)'",
        "'POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0), (2 2, 2 4, 4 4, 4 2, 2 2))', 'LINESTRING(0 0,0 10,10 10,10 0,0 0)', 'LINESTRING(2 2,2 4,4 4,4 2,2 2)'",
        "'POLYGON ((0 0, 0 10, 10 10, 10 0, 0 0), (2 2, 2 4, 4 4, 4 2, 2 2), (6 6, 6 8, 8 8, 8 6, 6 6))', 'LINESTRING(0 0,0 10,10 10,10 0,0 0)', 'LINESTRING(2 2,2 4,4 4,4 2,2 2)', 'LINESTRING(6 6,6 8,8 8,8 6,6 6)'"
    })
    void testPolygon(String expected, @AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(POLYGON(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'POINT(0 0)'",
        "'POLYGON((0 0,0 1,1 1,0 0))'",
        "'LINESTRING(0 0,1 1)'",
        "'LINESTRING(0 0,0 1,1 1,0 0)', 'POINT(0 0)'"
    })
    void testPolygonReturnsNull(@AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(POLYGON(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL|Illegal non geometric 'NULL' value found during parsing",
        "1|Illegal parameter data type INTEGER for operation 'polygon'",
        "1.5|Illegal parameter data type NUMERIC for operation 'polygon'",
        "'LINESTRING(0 0,1 1,0 0)'|Illegal parameter data type CHARACTER VARYING for operation 'polygon'",
        "ST_GeomFromText('LINESTRING(0 0,1 1,0 0)'), NULL|Illegal non geometric 'NULL' value found during parsing"
    }, delimiter = '|', quoteCharacter = '"')
    void testPolygonFailsForNonGeometry(String arguments, String expectedMessage) {
        verifyQueryError("SELECT POLYGON(" + arguments + ")", expectedMessage);
    }
}
