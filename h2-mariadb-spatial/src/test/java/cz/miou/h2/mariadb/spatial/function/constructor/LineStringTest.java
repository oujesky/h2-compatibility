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
 * Test for {@link cz.miou.h2.mariadb.spatial.function.constructor.LineString}.
 */
class LineStringTest {

    @ParameterizedTest
    @CsvSource({
        "'LINESTRING (0 0, 1 1)', 'POINT(0 0)', 'POINT(1 1)'",
        "'LINESTRING (0 0, 1 1, 2 3)', 'POINT(0 0)', 'POINT(1 1)', 'POINT(2 3)'"

        // MariaDB supports this case but underlying org.locationtech.jts.geom.LineString requires at least 2 points
        // "'LINESTRING (0 0)', 'POINT(0 0)'",
    })
    void testLineString(String expected, @AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(LINESTRING(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'POINT(0 0)', 'LINESTRING(0 0,1 1)'",
        "'POINT(0 0)', 'POLYGON((0 0,0 1,1 1,0 0))'",
        "'MULTIPOINT((0 0),(1 1))'"
    })
    void testLineStringReturnsNull(@AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(LINESTRING(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL|Illegal non geometric 'NULL' value found during parsing",
        "1|Illegal parameter data type INTEGER for operation 'linestring'",
        "1.5|Illegal parameter data type NUMERIC for operation 'linestring'",
        "'POINT(0 0)'|Illegal parameter data type CHARACTER VARYING for operation 'linestring'",
        "ST_GeomFromText('POINT(0 0)'), NULL|Illegal non geometric 'NULL' value found during parsing",

        // MariaDB supports this case but underlying org.locationtech.jts.geom.LineString requires at least 2 points
        "ST_GeomFromText('POINT(0 0)')|Invalid number of points in LineString (found 1 - must be 0 or >= 2)",
    }, delimiter = '|', quoteCharacter = '"')
    void testLineStringFailsForNonGeometry(String arguments, String expectedMessage) {
        verifyQueryError("SELECT LINESTRING(" + arguments + ")", expectedMessage);
    }
}
