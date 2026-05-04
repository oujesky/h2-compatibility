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
 * Test for {@link cz.miou.h2.mariadb.spatial.function.constructor.MultiLineString}.
 */
class MultiLineStringTest {

    @ParameterizedTest
    @CsvSource({
        "'MULTILINESTRING ((0 0, 1 1), (2 2, 3 3))', 'LINESTRING(0 0,1 1)', 'LINESTRING(2 2,3 3)'",
        "'MULTILINESTRING ((0 40, 0 20), (6 30, 12 20, 12 40))', 'LINESTRING(0 40,0 20)', 'LINESTRING(6 30,12 20,12 40)'",
        "'MULTILINESTRING ((0 0, 1 1, 2 2))', 'LINESTRING(0 0,1 1,2 2)'"
    })
    void testMultiLineString(String expected, @AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(MULTILINESTRING(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'POINT(0 0)'",
        "'POLYGON((0 0,0 1,1 1,0 0))'"
    })
    void testMultiLineStringReturnsNull(@AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(MULTILINESTRING(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL|Illegal non geometric 'NULL' value found during parsing",
        "1|Illegal parameter data type INTEGER for operation 'multilinestring'",
        "1.5|Illegal parameter data type NUMERIC for operation 'multilinestring'",
        "'LINESTRING(0 0,1 1)'|Illegal parameter data type CHARACTER VARYING for operation 'multilinestring'",
        "ST_GeomFromText('LINESTRING(0 0, 1 1)'), NULL|Illegal non geometric 'NULL' value found during parsing"
    }, delimiter = '|', quoteCharacter = '"')
    void testMultiLineStringFailsForNonGeometry(String arguments, String expectedMessage) {
        verifyQueryError("SELECT MULTILINESTRING(" + arguments + ")", expectedMessage);
    }

}
