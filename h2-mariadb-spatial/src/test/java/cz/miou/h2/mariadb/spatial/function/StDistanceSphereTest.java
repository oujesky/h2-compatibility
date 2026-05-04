package cz.miou.h2.mariadb.spatial.function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.assertj.core.data.Offset;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.function.StDistanceSphere}.
 */
class StDistanceSphereTest {

    private static final Offset<Double> OFFSET = Offset.offset(0.0001);

    @ParameterizedTest
    @CsvSource({
        "'POINT(0 0)', 'POINT(0 0)', 0",
        "'POINT(0 0)', 'POINT(1 1)', 157249.0357231545",
        "'POINT(-1 -1)', 'POINT(-2 -2)', 157225.0865419108",
        "'POINT(17.907743 44.203438)', 'POINT(18.413076 43.856258)', 55878.59337591705",
        "'POINT(0 1)', 'POINT(1 2)', 157225.0865419108",
        "'POINT(0 1)', 'POINT(2 1)', 222355.4901806686",
        "'POINT(1 0)', 'POINT(1 2)', 222389.3645969269",
        "'POINT(1 0)', 'POINT(2 1)', 157249.0357231545",
        "'POINT(0 0)', 'MULTIPOINT(1 1)', 157249.0357231545",
        "'MULTIPOINT(1 1)', 'POINT(0 0)', 157249.0357231545",
        "'POINT(0 0)', 'MULTIPOINT(1 1,2 2)', 157249.0357231545",
        "'POINT(0 0)', 'MULTIPOINT(2 2,1 1)', 157249.0357231545",
        "'MULTIPOINT(3 4,8 9)', 'MULTIPOINT(3 4,8 9)', 0",
        "'MULTIPOINT(1 2,1 1)', 'MULTIPOINT(3 4,8 9)', 314282.5644496733",
        "'MULTIPOINT(1 2,1 1)', 'MULTIPOINT(8 9,3 4)', 314282.5644496733"
    })
    void testDistanceSphere(String g1, String g2, double expected) throws SQLException {
        verifyQuery(
            "SELECT ST_DISTANCE_SPHERE(ST_GeomFromText(?), ST_GeomFromText(?))",
            statement -> {
                statement.setString(1, g1);
                statement.setString(2, g2);
            },
            rs -> assertThat(rs.getDouble(1)).isCloseTo(expected, OFFSET)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "'POINT(0 0)', 'POINT(0 0)', 1, 0",
        "'POINT(0 0)', 'POINT(1 1)', 1, 0.024682056391766436",
        "'POINT(17.907743 44.203438)', 'POINT(18.413076 43.856258)', 6370986, 55878.59337591705",
        "'POINT(17.907743 44.203438)', 'POINT(18.413076 43.856258)', 200, 1.754158410516584",
        "'POINT(0 0)', 'MULTIPOINT(1 1,2 2)', 1, 0.024682056391766436",
        "'POINT(0 0)', 'MULTIPOINT(2 2,1 1)', 1, 0.024682056391766436",
        "'MULTIPOINT(1 2,1 1)', 'MULTIPOINT(8 9,3 4)', 1, 0.04933028646581131"
    })
    void testDistanceSphereWithRadius(String g1, String g2, double radius, double expected) throws SQLException {
        verifyQuery(
            "SELECT ST_DISTANCE_SPHERE(ST_GeomFromText(?), ST_GeomFromText(?), ?)",
            statement -> {
                statement.setString(1, g1);
                statement.setString(2, g2);
                statement.setDouble(3, radius);
            },
            rs -> assertThat(rs.getDouble(1)).isCloseTo(expected, OFFSET)
        );
    }

    @Test
    void testDistanceSphereIgnoresSrid() throws SQLException {
        verifyQuery(
            "SELECT ST_DISTANCE_SPHERE(ST_GeomFromText('POINT(0 0)', 0), ST_GeomFromText('POINT(1 1)', 4326))",
            rs -> assertThat(rs.getDouble(1)).isCloseTo(157249.0357231545, OFFSET)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL, ST_GeomFromText('POINT(0 0)')",
        "ST_GeomFromText('POINT(0 0)'), NULL",
        "ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('POINT(1 1)'), NULL"
    }, delimiter = '|', quoteCharacter = '"')
    void testDistanceSphereReturnsNull(String arguments) throws SQLException {
        verifyQuery(
            "SELECT ST_DISTANCE_SPHERE(" + arguments + ")",
            rs -> assertThat(rs.getObject(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "ST_GeomFromText('LINESTRING(0 0,1 1)'), ST_GeomFromText('POINT(0 0)')|Calling geometry function st_distance_sphere with unsupported types of arguments.",
        "ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('LINESTRING(0 0,1 1)')|Calling geometry function st_distance_sphere with unsupported types of arguments.",
        "ST_GeomFromText('POLYGON((0 0,0 1,1 1,0 0))'), ST_GeomFromText('POINT(0 0)')|Calling geometry function st_distance_sphere with unsupported types of arguments.",
        "ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('GEOMETRYCOLLECTION(POINT(1 1))')|Calling geometry function st_distance_sphere with unsupported types of arguments."
    }, delimiter = '|', quoteCharacter = '"')
    void testDistanceSphereFailsForUnsupportedGeometryTypes(String arguments, String expectedMessage) {
        verifyQueryError("SELECT ST_DISTANCE_SPHERE(" + arguments + ")", expectedMessage);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "1, ST_GeomFromText('POINT(0 0)')|Invalid GIS data provided to function ST_Distance_Sphere.",
        "ST_GeomFromText('POINT(0 0)'), 1|Invalid GIS data provided to function ST_Distance_Sphere.",
        "1.5, ST_GeomFromText('POINT(0 0)')|Invalid GIS data provided to function ST_Distance_Sphere.",
        "ST_GeomFromText('POINT(0 0)'), 1.5|Invalid GIS data provided to function ST_Distance_Sphere.",
        "'POINT(0 0)', ST_GeomFromText('POINT(1 1)')|Invalid GIS data provided to function ST_Distance_Sphere.",
        "ST_GeomFromText('POINT(0 0)'), 'POINT(1 1)'|Invalid GIS data provided to function ST_Distance_Sphere."
    }, delimiter = '|', quoteCharacter = '"')
    void testDistanceSphereFailsForNonGeometryArguments(String arguments, String expectedMessage) {
        verifyQueryError("SELECT ST_DISTANCE_SPHERE(" + arguments + ")", expectedMessage);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('POINT(1 1)'), 'abc'|Calling geometry function st_distance_sphere with unsupported types of arguments.",
        "ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('POINT(1 1)'), ST_GeomFromText('POINT(2 2)')|Calling geometry function st_distance_sphere with unsupported types of arguments."
    }, delimiter = '|', quoteCharacter = '"')
    void testDistanceSphereFailsForNonNumericRadius(String arguments, String expectedMessage) {
        verifyQueryError("SELECT ST_DISTANCE_SPHERE(" + arguments + ")", expectedMessage);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('POINT(1 1)'), 0|Calling geometry function st_distance_sphere with unsupported types of arguments.",
        "ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('POINT(1 1)'), -1|Calling geometry function st_distance_sphere with unsupported types of arguments.",
        "ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('POINT(1 1)'), -0.1|Calling geometry function st_distance_sphere with unsupported types of arguments."
    }, delimiter = '|', quoteCharacter = '"')
    void testDistanceSphereFailsForNonPositiveRadius(String arguments, String expectedMessage) {
        verifyQueryError("SELECT ST_DISTANCE_SPHERE(" + arguments + ")", expectedMessage);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "ST_GeomFromText('POINT(181 0)'), ST_GeomFromText('POINT(0 0)')|Out of range error: Longitude should be [-180,180] in function ST_Distance_Sphere.",
        "ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('POINT(-181 0)')|Out of range error: Longitude should be [-180,180] in function ST_Distance_Sphere.",
        "ST_GeomFromText('POINT(0 91)'), ST_GeomFromText('POINT(0 0)')|Out of range error: Latitude should be [-90,90] in function ST_Distance_Sphere.",
        "ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('POINT(0 -91)')|Out of range error: Latitude should be [-90,90] in function ST_Distance_Sphere."
    }, delimiter = '|', quoteCharacter = '"')
    void testDistanceSphereFailsForOutOfRangeCoordinates(String arguments, String expectedMessage) {
        verifyQueryError("SELECT ST_DISTANCE_SPHERE(" + arguments + ")", expectedMessage);
    }

}
