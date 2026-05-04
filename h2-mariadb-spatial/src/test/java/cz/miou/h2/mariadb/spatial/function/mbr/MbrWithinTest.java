package cz.miou.h2.mariadb.spatial.function.mbr;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.function.mbr.MbrWithin}.
 */
class MbrWithinTest {

    @ParameterizedTest
    @CsvSource({
        "'POINT(5 5)', 'POLYGON((0 0,0 10,10 10,10 0,0 0))', true",
        "'POLYGON((0 0,0 3,3 3,3 0,0 0))', 'POLYGON((0 0,0 5,5 5,5 0,0 0))', true",
        "'POLYGON((0 0,0 5,5 5,5 0,0 0))', 'POLYGON((0 0,0 3,3 3,3 0,0 0))', false",
        "'POLYGON((0 0,0 2,2 2,2 0,0 0))', 'POLYGON((0 0,0 3,3 3,3 0,0 0))', true",
        "'POLYGON((0 0,0 2,2 2,2 0,0 0))', 'POLYGON((0 0,0 1,1 1,1 0,0 0))', false"
    })
    void testMbrWithin(String g1Wkt, String g2Wkt, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT MBRWithin(ST_GeomFromText(?), ST_GeomFromText(?))",
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
    void testMbrWithinReturnsNull(String arguments) throws SQLException {
        verifyQuery(
            "SELECT MBRWithin(" + arguments + ")",
            rs -> assertThat(rs.getObject(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "1, ST_GeomFromText('POINT(0 0)')|Illegal parameter data type INTEGER for operation 'mbrwithin'",
        "ST_GeomFromText('POINT(0 0)'), 1|Illegal parameter data type INTEGER for operation 'mbrwithin'",
        "'POINT(0 0)', ST_GeomFromText('POINT(0 0)')|Illegal parameter data type CHARACTER VARYING for operation 'mbrwithin'",
        "ST_GeomFromText('POINT(0 0)'), 'POINT(0 0)'|Illegal parameter data type CHARACTER VARYING for operation 'mbrwithin'"
    }, delimiter = '|', quoteCharacter = '"')
    void testMbrWithinFailsForNonGeometry(String arguments, String expectedMessage) {
        verifyQueryError("SELECT MBRWithin(" + arguments + ")", expectedMessage);
    }
}
