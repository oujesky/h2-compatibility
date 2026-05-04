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
 * Test for {@link cz.miou.h2.mariadb.spatial.function.constructor.GeometryCollection}.
 */
class GeometryCollectionTest {

    @ParameterizedTest
    @CsvSource({
        "'GEOMETRYCOLLECTION (POINT (0 0))', 'POINT(0 0)'",
        "'GEOMETRYCOLLECTION (POINT (0 0), LINESTRING (0 0, 1 1))', 'POINT(0 0)', 'LINESTRING(0 0,1 1)'",
        "'GEOMETRYCOLLECTION (POINT (0 0), LINESTRING (0 0, 1 1), POINT (1 2))', 'POINT(0 0)', 'LINESTRING(0 0,1 1)', 'POINT(1 2)'",
    })
    void testGeometryCollection(String expected, @AggregateWith(WktAggregator.class) WktGeometries geometries) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(GEOMETRYCOLLECTION(" + geometries.getPlaceholders() + "))",
            geometries::bind,
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL|Illegal non geometric 'NULL' value found during parsing",
        "1|Illegal parameter data type INTEGER for operation 'geometrycollection'",
        "1.5|Illegal parameter data type NUMERIC for operation 'geometrycollection'",
        "'POINT(0 0)'|Illegal parameter data type CHARACTER VARYING for operation 'geometrycollection'",
        "ST_GeomFromText('POINT(0 0)'), NULL|Illegal non geometric 'NULL' value found during parsing"
    }, delimiter = '|', quoteCharacter = '"')
    void testGeometryCollectionFailsForNonGeometry(String arguments, String expectedMessage) {
        verifyQueryError("SELECT GEOMETRYCOLLECTION(" + arguments + ")", expectedMessage);
    }

}
