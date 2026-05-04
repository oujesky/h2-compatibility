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
 * Test for {@link cz.miou.h2.mariadb.spatial.function.constructor.MultiPoint}.
 */
class MultiPointTest {

    @ParameterizedTest
    @CsvSource({
        "'MULTIPOINT ((0 0), (1 1))', 'POINT(0 0)', 'POINT(1 1)'",
        "'MULTIPOINT ((0 0), (1 0), (1 1), (0 1))', 'POINT(0 0)', 'POINT(1 0)', 'POINT(1 1)', 'POINT(0 1)'",
        "'MULTIPOINT ((1 1), (2 2), (3 3), (4 4))', 'POINT(1 1)', 'POINT(2 2)', 'POINT(3 3)', 'POINT(4 4)'"
    })
    void testMultiPoint(String expected, @AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(MULTIPOINT(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'LINESTRING(0 0,1 1)'",
        "'POLYGON((0 0,0 1,1 1,0 0))'"
    })
    void testMultiPointReturnsNull(@AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(MULTIPOINT(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL|Illegal non geometric 'NULL' value found during parsing",
        "1|Illegal parameter data type INTEGER for operation 'multipoint'",
        "1.5|Illegal parameter data type NUMERIC for operation 'multipoint'",
        "'POINT(0 0)'|Illegal parameter data type CHARACTER VARYING for operation 'multipoint'",
        "ST_GeomFromText('POINT(0 0)'), NULL|Illegal non geometric 'NULL' value found during parsing"
    }, delimiter = '|', quoteCharacter = '"')
    void testMultiPointFailsForNonGeometry(String arguments, String expectedMessage) {
        verifyQueryError("SELECT MULTIPOINT(" + arguments + ")", expectedMessage);
    }
}
