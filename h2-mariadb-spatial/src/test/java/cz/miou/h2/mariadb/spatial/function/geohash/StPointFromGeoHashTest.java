package cz.miou.h2.mariadb.spatial.function.geohash;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.function.geohash.StPointFromGeoHash}.
 */
class StPointFromGeoHashTest {

    @ParameterizedTest
    @CsvSource(value = {
        "'ezs42', 0|POINT (-5.6 42.6)",
        "'s00twy01mtw037m', 0|POINT (1 1)",
        "'0', 0|POINT (-158 -68)",
        "'z', 0|POINT (158 68)",
        "'0z', 0|POINT (-141 -48)",
        "'xbpb', 0|POINT (180 0)",
        "'8000', 0|POINT (-180 0)",
        "'s000', 0|POINT (0 0)",
        "'0123456789', 0|POINT (-179.55743 -82.774507)",
        "'9876543210', 0|POINT (-107.79609 1.770175)",
        "'bcdefghjkmnpqrstuvwxyz', 0|POINT (-142.6078415216915 54.11408847964353)",
        "'zyxwvutsrqpnmkjhgfedcb', 0|POINT (179.55743439816956 82.77450549262497)",
        "'0123456789BCDEFGHJKMNPQRSTUVWXYZ', 0|POINT (-179.55743439816956 -82.77450549262497)",
        "'ZYXWVUTSRQPNMKJHGFEDCB9876543210', 0|POINT (179.55743439816956 82.77450549262497)",
        "'zzzzzzzzzzzzzzzzzzzz', 0|POINT (180 90)",
        "'bpbpbpbpbpbpbpbpbpbp', 0|POINT (-180 90)",
        "'pbpbpbpbpbpbpbpbpbpb', 0|POINT (180 -90)",
        "'00000000000000000000', 0|POINT (-180 -90)",
        "'gzzzzzzzzzzzzzzzzzzz', 0|POINT (0 90)",
        "'5bpbpbpbpbpbpbpbpbpb', 0|POINT (0 -90)",
        "'7zzzzzzzzzzzzzzzzzzz', 0|POINT (0 0)",
        "'rzzzzzzzzzzzzzzzzzzz', 0|POINT (180 0)",
        "'2pbpbpbpbpbpbpbpbpbp', 0|POINT (-180 0)",
        "'0000000000zzzzzzzzzz', 0|POINT (-179.999989271164 -89.999994635582)",
        "'zzzzzzzzzz0000000000', 0|POINT (179.999989271164 89.999994635582)",
        "'s000000001z7wsg7zzm6', 0|POINT (0.00001 0.00001)",
        "'ebpbpbpbpcbe9kuebp6d', 0|POINT (-0.00001 0.00001)",
        "'kpbpbpbpbnpkqe5kpbtm', 0|POINT (0.00001 -0.00001)",
        "'7zzzzzzzzy0s37hs00dt', 0|POINT (-0.00001 -0.00001)",
        "'tzzzzzzzzzzzzzzzzzzz', 0|POINT (90 45)",
        "'9zzzzzzzzzzzzzzzzzzz', 0|POINT (-90 45)",
        "'jzzzzzzzzzzzzzzzzzzz', 0|POINT (90 -45)",
        "'1zzzzzzzzzzzzzzzzzzz', 0|POINT (-90 -45)",
        "'zbzurypzpgxczbzurypz', 0|POINT (180 50)",
        "'5zpgxczbzurypzpgxczb', 0|POINT (0 -50)",
        "'100', '    0'|POINT (-134 -89)",
        "CAST(100 AS VARCHAR), 0|POINT (-134 -89)",
        "'10111000110001111001', '0'|POINT (-133.549761770805 -89.8242133801793)",
        "'11111111111111111111', 0|POINT (-133.548387096774 -84.1935483870967)",
        "'99999999999999999999', 0|POINT (-110.3225806451612 8.7096774193548)",
        "'00000000000000000000', ' 0 '|POINT (-180 -90)",
        "'zzzzzzzzzzzzzzzzzzzz', CAST('0' AS CHAR)|POINT (180 90)",
        "CAST('012' AS VARBINARY), 0|POINT (-179 -82)",
    }, delimiter = '|', quoteCharacter = '"')
    void testPointFromGeoHash(String arguments, String expected) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(ST_PointFromGeoHash(" + arguments + "))",
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "'0123', 4326|4326",
        "'0123', '4326'|4326",
    }, delimiter = '|', quoteCharacter = '"')
    void testPointFromGeoHashSrid(String arguments, int expected) throws SQLException {
        verifyQuery(
            "SELECT ST_SRID(ST_PointFromGeoHash(" + arguments + "))",
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL, 0",
        "'tzzzzzzzzzzzzzzzzzzz', NULL"
    }, delimiter = '|', quoteCharacter = '"')
    void testPointFromGeoHashReturnsNull(String arguments) throws SQLException {
        verifyQuery(
            "SELECT ST_AsText(ST_PointFromGeoHash(" + arguments + "))",
            rs -> assertThat(rs.getObject(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "'0123a45', 0|Incorrect geohash value: '0123a45' for function ST_PointFromGeoHash",
        "'xyzi', 0|Incorrect geohash value: 'xyzi' for function ST_PointFromGeoHash",
        "'zyxLwv', 0|Incorrect geohash value: 'zyxLwv' for function ST_PointFromGeoHash",
        "'bcdjo', 0|Incorrect geohash value: 'bcdjo' for function ST_PointFromGeoHash",
        "'zyx**wv', 0|Incorrect geohash value: 'zyx**wv' for function ST_PointFromGeoHash",
        "'1 2 3 4', 0|Incorrect geohash value: '1 2 3 4' for function ST_PointFromGeoHash",
        "'12.345', 0|Incorrect geohash value: '12.345' for function ST_PointFromGeoHash",
        "'   ', 0|Incorrect geohash value: '   ' for function ST_PointFromGeoHash",
        "'NULL', 0|Incorrect geohash value: 'NULL' for function ST_PointFromGeoHash",
        "'0a 0d', 0|Incorrect geohash value: '0a 0d' for function ST_PointFromGeoHash",
        "'-100', 0|Incorrect geohash value: '-100' for function ST_PointFromGeoHash",
        "'', 2000|Incorrect geohash value: '' for function ST_PointFromGeoHash"
    }, delimiter = '|', quoteCharacter = '"')
    void testPointFromGeoHashFailsForInvalidGeoHash(String arguments, String expectedMessage) {
        verifyQueryError("SELECT ST_AsText(ST_PointFromGeoHash(" + arguments + "))", expectedMessage);
    }

}
