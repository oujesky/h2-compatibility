package cz.miou.h2.mariadb.spatial.function;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.function.StValidate}.
 */
class StValidateTest {

    @ParameterizedTest
    @CsvSource({
        "'LINESTRING(0 0,1 1)', 'LINESTRING (0 0, 1 1)'",
        "'POINT(1 0)', 'POINT (1 0)'"
    })
    void testValidate(String wkt, String expected) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(ST_Validate(ST_GeomFromText(?)))",
            statement -> statement.setString(1, wkt),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL",
        "ST_GeomFromText('POLYGON((0 0,1 1,1 0,0 1,0 0))')",
        "ST_GeomFromText('POLYGON((0 0,0 10,10 10,10 0,0 0),(20 20,21 20,21 21,20 20))')",
        "ST_GeomFromText('MULTIPOLYGON(((0 0,1 1,1 0,0 1,0 0)))')"
    }, delimiter = '|', quoteCharacter = '"')
    void testValidateReturnsNull(String argument) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(ST_Validate(" + argument + "))",
            rs -> assertThat(rs.getString(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "ST_GeomFromText('POINT(181 0)')|Out of range error: Longitude should be [-180,180] in function ST_Validate.",
        "ST_GeomFromText('POINT(-181 0)')|Out of range error: Longitude should be [-180,180] in function ST_Validate.",
        "ST_GeomFromText('POINT(0 91)')|Out of range error: Latitude should be [-90,90] in function ST_Validate.",
        "ST_GeomFromText('POINT(0 -91)')|Out of range error: Latitude should be [-90,90] in function ST_Validate."
    }, delimiter = '|', quoteCharacter = '"')
    void testValidateFailsForOutOfRangePointCoordinates(String argument, String expectedMessage) {
        verifyQueryError("SELECT ST_Validate(" + argument + ")", expectedMessage);
    }
}
