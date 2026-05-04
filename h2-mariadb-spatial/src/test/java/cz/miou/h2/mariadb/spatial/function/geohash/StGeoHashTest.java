package cz.miou.h2.mariadb.spatial.function.geohash;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQueryError;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.function.geohash.StGeoHash}.
 */
class StGeoHashTest {

    @ParameterizedTest
    @CsvSource({
        "'POINT(-5.60302734375 42.593994140625)', 5, 'ezs42'",
        "'POINT(1 1)', 15, 's00twy01mtw037m'",
        "'POINT(0 0)', 10, 's000000000'",
        "'POINT(180 0)', 20, 'xbpbpbpbpbpbpbpbpbpb'",
        "'POINT(-180 0)', 25, '8000000000000000000002pbp'",
        "'POINT(0 90)', 30, 'upbpbpbpbpbpbpbpbpbpbpbpbpbpbp'",
        "'POINT(0 -90)', 35, 'h000000000000000000001bpbpbpbpbpbpb'",
        "'POINT(180 90)', 40, 'zzzzzzzzzzzzzzzzzzzzzz'",
        "'POINT(180 -90)', 45, 'pbpbpbpbpbpbpbpbpbpbpc'",
        "'POINT(-180 90)', 50, 'bpbpbpbpbpbpbpbpbpbpbr'",
        "'POINT(-180 -90)', 55, '0000000000000000000003'",
        "'POINT(100 45)', 60, 'y0p0581b0bh2n0p0581b0b'",
        "'POINT(100 -45)', 65, 'q0p0581b0bh2n0p0581b0b'",
        "'POINT(-100 45)', 70, 'cb0bh2n0p0581b0bh2n0p8'",
        "'POINT(-100 -45)', 75, '3b0bh2n0p0581b0bh2n0p8'",
        "'POINT(-100 -45)', 100, '3b0bh2n0p0581b0bh2n0p8'"
    })
    void testGeoHash(String wkt, int maxLength, String expected) throws SQLException {
        verifyQuery(
            "SELECT ST_GeoHash(ST_GeomFromText(?), ?)",
            statement -> {
                statement.setString(1, wkt);
                statement.setInt(2, maxLength);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "0, 30, 15|sj248j248j248j2",
        "0, 0, 1|s",
        "180, 0, 20|xbpbpbpbpbpbpbpbpbpb",
        "-180, 0, 20|80000000000000000000",
        "0, 90, 20|upbpbpbpbpbpbpbpbpbp",
        "0, -90, 20|h0000000000000000000",
        "180, 90, 20|zzzzzzzzzzzzzzzzzzzz",
        "180, -90, 20|pbpbpbpbpbpbpbpbpbpb",
        "-180, 90, 20|bpbpbpbpbpbpbpbpbpbp",
        "-180, -90, 20|00000000000000000000",
        "100, 45, 5|y0p05",
        "100, -45, 10|q0p0581b0b",
        "-100, 45, +50|cb0bh2n0p0581b0bh2n0p8",
        "0.0001, 0.0001, 10|s0000000d6",
        "0.0001, -0.0001, 30|kpbpbpbp6m6e6p6bpntebbpkpnqbuf",
        "'0', '0', '10'|s000000000",
        "180, 90, '20'|zzzzzzzzzzzzzzzzzzzz",
        "'180', '90', '20'|zzzzzzzzzzzzzzzzzzzz",
    }, delimiter = '|', quoteCharacter = '"')
    void testGeoHashFromCoordinates(String arguments, String expected) throws SQLException {
        verifyQuery(
            "SELECT ST_GeoHash(" + arguments + ")",
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "NULL, 90, 10",
        "180, NULL, 10",
        "180, 90, NULL",
        "ST_GeomFromText('POINT(0 0)'), NULL"
    }, delimiter = '|', quoteCharacter = '"')
    void testGeoHashReturnsNull(String arguments) throws SQLException {
        verifyQuery(
            "SELECT ST_GeoHash(" + arguments + ")",
            rs -> assertThat(rs.getObject(1)).isNull()
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "181, 0, 10|Out of range error: Longitude should be [-180,180] in function ST_GeoHash.",
        "1000, 90, 10|Out of range error: Longitude should be [-180,180] in function ST_GeoHash.",
        "180.0000000000001, -90, 10|Out of range error: Longitude should be [-180,180] in function ST_GeoHash.",
        "-181, 0, 10|Out of range error: Longitude should be [-180,180] in function ST_GeoHash.",
        "-1000, 90, 10|Out of range error: Longitude should be [-180,180] in function ST_GeoHash.",
        "-180.0000000000001, -90, 10|Out of range error: Longitude should be [-180,180] in function ST_GeoHash.",
        "0, 91, 10|Out of range error: Latitude should be [-90,90] in function ST_GeoHash.",
        "180, 500, 10|Out of range error: Latitude should be [-90,90] in function ST_GeoHash.",
        "-180, 90.0000000000001, 10|Out of range error: Latitude should be [-90,90] in function ST_GeoHash.",
        "0, -91, 10|Out of range error: Latitude should be [-90,90] in function ST_GeoHash.",
        "180, -500, 10|Out of range error: Latitude should be [-90,90] in function ST_GeoHash.",
        "-180, -90.0000000000001, 10|Out of range error: Latitude should be [-90,90] in function ST_GeoHash.",
        "'181', 90, 20|Out of range error: Longitude should be [-180,180] in function ST_GeoHash.",
        "'-181', 90, 20|Out of range error: Longitude should be [-180,180] in function ST_GeoHash.",
        "180, '91', 20|Out of range error: Latitude should be [-90,90] in function ST_GeoHash.",
        "180, '-91', 20|Out of range error: Latitude should be [-90,90] in function ST_GeoHash."
    }, delimiter = '|', quoteCharacter = '"')
    void testGeoHashFailsForOutOfRangeCoordinates(String arguments, String expectedMessage) {
        verifyQueryError("SELECT ST_GeoHash(" + arguments + ")", expectedMessage);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "180, 90, 0|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "-180, 90, -1|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "180, -90, -100|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "-180, -90, 101|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "0, 90, 1000|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "180, 90, '0'|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "180, 90, '-1'|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "180, 90, '101'|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "180, 90, ''|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "180, 90, '****'|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "180, 90, 20.0001|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
    }, delimiter = '|', quoteCharacter = '"')
    void testGeoHashFailsForInvalidMaxLength(String arguments, String expectedMessage) {
        verifyQueryError("SELECT ST_GeoHash(" + arguments + ")", expectedMessage);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "ST_GeomFromText('POINT(181 0)'), 10|Out of range error: Longitude should be [-180,180] in function ST_GeoHash.",
        "ST_GeomFromText('POINT(0 91)'), 10|Out of range error: Latitude should be [-90,90] in function ST_GeoHash.",
        "ST_GeomFromText('POINT(180 90)'), 0|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash.",
        "ST_GeomFromText('POINT(180 90)'), 20.0001|Out of range error: max_length parameter should be an integer in the range [1, 100] in function ST_GeoHash."
    }, delimiter = '|', quoteCharacter = '"')
    void testGeoHashGeometryOverloadFailsForOutOfRangeArguments(String arguments, String expectedMessage) {
        verifyQueryError("SELECT ST_GeoHash(" + arguments + ")", expectedMessage);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "ST_GeomFromText('LINESTRING(0 0,10 10)'), 10|Invalid GIS data provided to function ST_GeoHash.",
        "ST_GeomFromText('POLYGON((0 0,10 0,10 10,0 10,0 0))'), 10|Invalid GIS data provided to function ST_GeoHash.",
        "ST_GeomFromText('MULTIPOINT(0 0,10 10)'), 10|Invalid GIS data provided to function ST_GeoHash.",
        "ST_GeomFromText('GEOMETRYCOLLECTION(POINT(0 0))'), 10|Invalid GIS data provided to function ST_GeoHash.",
        "1, 2|Invalid GIS data provided to function ST_GeoHash.",
        "CAST('2024-01-01' AS DATE), 90, 10|Invalid GIS data provided to function ST_GeoHash.",
        "180, CAST('2024-01-01' AS DATE), 10|Invalid GIS data provided to function ST_GeoHash."
    }, delimiter = '|', quoteCharacter = '"')
    void testGeoHashFailsForInvalidGisData(String arguments, String expectedMessage) {
        verifyQueryError("SELECT ST_GeoHash(" + arguments + ")", expectedMessage);
    }
}
