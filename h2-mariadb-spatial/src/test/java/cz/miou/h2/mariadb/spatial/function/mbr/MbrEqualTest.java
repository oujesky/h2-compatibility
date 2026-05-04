package cz.miou.h2.mariadb.spatial.function.mbr;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.function.mbr.MbrEqual}.
 */
class MbrEqualTest {

    @ParameterizedTest
    @CsvSource({
        "'LINESTRING(0 0,1 1)', 'LINESTRING(0 0,1 1)', true",
        "'LINESTRING(0 0,1 2)', 'POLYGON((0 0,0 2,1 2,1 0,0 0))', true",
        "'LINESTRING(0 0,1 3)', 'POLYGON((0 0,0 2,1 4,1 0,0 0))', false",
        "'POLYGON((0 0,0 2,2 2,2 0,0 0))', 'POLYGON((0 0,0 2,2 2,2 0,0 0))', true",
        "'POLYGON((0 0,0 2,2 2,2 0,0 0))', 'POLYGON((0 0,0 1,1 1,1 0,0 0))', false"
    })
    void testMbrEqual(String g1Wkt, String g2Wkt, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT MBREqual(ST_GeomFromText(?), ST_GeomFromText(?))",
            statement -> {
                statement.setString(1, g1Wkt);
                statement.setString(2, g2Wkt);
            },
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL, ST_GeomFromText('POINT(0 0)')",
        "ST_GeomFromText('POINT(0 0)'), NULL",
    }, delimiter = '|', quoteCharacter = '"')
    void testMbrEqualReturnsNull(String arguments) throws SQLException {
        verifyQuery(
            "SELECT MBREqual(" + arguments + ")",
            rs -> assertThat(rs.getObject(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "1, ST_GeomFromText('POINT(0 0)')|Illegal parameter data type INTEGER for operation 'mbrequal'",
        "ST_GeomFromText('POINT(0 0)'), 1|Illegal parameter data type INTEGER for operation 'mbrequal'",
        "'POINT(0 0)', ST_GeomFromText('POINT(0 0)')|Illegal parameter data type CHARACTER VARYING for operation 'mbrequal'",
        "ST_GeomFromText('POINT(0 0)'), 'POINT(0 0)'|Illegal parameter data type CHARACTER VARYING for operation 'mbrequal'"
    }, delimiter = '|', quoteCharacter = '"')
    void testMbrEqualFailsForNonGeometry(String arguments, String expectedMessage) {
        verifyQueryError("SELECT MBREqual(" + arguments + ")", expectedMessage);
    }

    @ParameterizedTest
    @CsvSource({
        "'LINESTRING(0 0,1 2)', 'POLYGON((0 0,0 2,1 2,1 0,0 0))', true",
        "'LINESTRING(0 0,1 3)', 'POLYGON((0 0,0 2,1 4,1 0,0 0))', false",
    })
    void testMbrEqualsAlias(String g1Wkt, String g2Wkt, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT MBREquals(ST_GeomFromText(?), ST_GeomFromText(?))",
            statement -> {
                statement.setString(1, g1Wkt);
                statement.setString(2, g2Wkt);
            },
            rs -> assertThat(rs.getBoolean(1)).isEqualTo(expected)
        );
    }
}
