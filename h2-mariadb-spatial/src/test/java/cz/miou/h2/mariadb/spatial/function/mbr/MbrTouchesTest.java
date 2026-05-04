package cz.miou.h2.mariadb.spatial.function.mbr;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.function.mbr.MbrTouches}.
 */
class MbrTouchesTest {

    @ParameterizedTest
    @CsvSource({
        "'POLYGON((0 0,0 1,1 1,1 0,0 0))', 'POLYGON((1 0,1 1,2 1,2 0,1 0))', true",
        "'POLYGON((0 0,0 3,3 3,3 0,0 0))', 'POLYGON((4 4,4 7,7 7,7 4,4 4))', false",
        "'POLYGON((0 0,0 3,3 3,3 0,0 0))', 'POLYGON((3 3,3 6,6 6,6 3,3 3))', true",
        "'POLYGON((0 0,0 4,4 4,4 0,0 0))', 'POLYGON((3 3,3 6,6 6,6 3,3 3))', false",
        "'POLYGON((0 0,0 2,2 2,2 0,0 0))', 'POLYGON((0 2,0 4,2 4,2 2,0 2))', true",
        "'POLYGON((0 0,0 2,2 2,2 0,0 0))', 'POLYGON((0 1,0 3,2 3,2 1,0 1))', false"
    })
    void testMbrTouches(String g1Wkt, String g2Wkt, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT MBRTouches(ST_GeomFromText(?), ST_GeomFromText(?))",
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
    void testMbrTouchesReturnsNull(String arguments) throws SQLException {
        verifyQuery(
            "SELECT MBRTouches(" + arguments + ")",
            rs -> assertThat(rs.getObject(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "1, ST_GeomFromText('POINT(0 0)')|Illegal parameter data type INTEGER for operation 'mbrtouches'",
        "ST_GeomFromText('POINT(0 0)'), 1|Illegal parameter data type INTEGER for operation 'mbrtouches'",
        "'POINT(0 0)', ST_GeomFromText('POINT(0 0)')|Illegal parameter data type CHARACTER VARYING for operation 'mbrtouches'",
        "ST_GeomFromText('POINT(0 0)'), 'POINT(0 0)'|Illegal parameter data type CHARACTER VARYING for operation 'mbrtouches'"
    }, delimiter = '|', quoteCharacter = '"')
    void testMbrTouchesFailsForNonGeometry(String arguments, String expectedMessage) {
        verifyQueryError("SELECT MBRTouches(" + arguments + ")", expectedMessage);
    }
}
