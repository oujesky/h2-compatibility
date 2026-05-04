package cz.miou.h2.mariadb.spatial.function.mbr;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.function.mbr.MbrDisjoint}.
 */
class MbrDisjointTest {

    @ParameterizedTest
    @CsvSource({
        "'POINT(0 0)', 'POINT(2 2)', true",
        "'POLYGON((0 0,0 3,3 3,3 0,0 0))', 'POLYGON((4 4,4 7,7 7,7 4,4 4))', true",
        "'POLYGON((0 0,0 3,3 3,3 0,0 0))', 'POLYGON((3 3,3 6,6 6,6 3,3 3))', false",
        "'POLYGON((0 0,0 2,2 2,2 0,0 0))', 'POLYGON((0 3,0 5,2 5,2 3,0 3))', true",
        "'POLYGON((0 0,0 2,2 2,2 0,0 0))', 'POLYGON((0 2,0 4,2 4,2 2,0 2))', false"
    })
    void testMbrDisjoint(String g1Wkt, String g2Wkt, boolean expected) throws SQLException {
        verifyQuery(
            "SELECT MBRDisjoint(ST_GeomFromText(?), ST_GeomFromText(?))",
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
    void testMbrDisjointReturnsNull(String arguments) throws SQLException {
        verifyQuery(
            "SELECT MBRDisjoint(" + arguments + ")",
            rs -> assertThat(rs.getObject(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "1, ST_GeomFromText('POINT(0 0)')|Illegal parameter data type INTEGER for operation 'mbrdisjoint'",
        "ST_GeomFromText('POINT(0 0)'), 1|Illegal parameter data type INTEGER for operation 'mbrdisjoint'",
        "'POINT(0 0)', ST_GeomFromText('POINT(0 0)')|Illegal parameter data type CHARACTER VARYING for operation 'mbrdisjoint'",
        "ST_GeomFromText('POINT(0 0)'), 'POINT(0 0)'|Illegal parameter data type CHARACTER VARYING for operation 'mbrdisjoint'"
    }, delimiter = '|', quoteCharacter = '"')
    void testMbrDisjointFailsForNonGeometry(String arguments, String expectedMessage) {
        verifyQueryError("SELECT MBRDisjoint(" + arguments + ")", expectedMessage);
    }
}
