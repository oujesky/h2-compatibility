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
 * Test for {@link cz.miou.h2.mariadb.spatial.function.constructor.MultiPolygon}.
 */
class MultiPolygonTest {

    @ParameterizedTest
    @CsvSource({
        "'MULTIPOLYGON (((0 0, 0 1, 1 1, 0 0)))', 'POLYGON((0 0,0 1,1 1,0 0))'",
        "'MULTIPOLYGON (((0 0, 0 1, 1 1, 1 0, 0 0)))', 'POLYGON((0 0,0 1,1 1,1 0,0 0))'"
    })
    void testMultiPolygon(String expected, @AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(MULTIPOLYGON(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'POINT(0 0)'",
        "'LINESTRING(0 0,1 1)'"
    })
    void testMultiPolygonReturnsNull(@AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(MULTIPOLYGON(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL|Illegal non geometric 'NULL' value found during parsing",
        "1|Illegal parameter data type INTEGER for operation 'multipolygon'",
        "1.5|Illegal parameter data type NUMERIC for operation 'multipolygon'",
        "'POLYGON((0 0,0 1,1 1,0 0))'|Illegal parameter data type CHARACTER VARYING for operation 'multipolygon'",
        "ST_GeomFromText('POLYGON((0 0,0 1,1 1,0 0))'), NULL|Illegal non geometric 'NULL' value found during parsing"
    }, delimiter = '|', quoteCharacter = '"')
    void testMultiPolygonFailsForNonGeometry(String arguments, String expectedMessage) {
        verifyQueryError("SELECT MULTIPOLYGON(" + arguments + ")", expectedMessage);
    }
}
