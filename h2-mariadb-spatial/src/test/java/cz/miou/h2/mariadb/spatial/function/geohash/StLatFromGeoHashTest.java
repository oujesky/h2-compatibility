package cz.miou.h2.mariadb.spatial.function.geohash;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.assertj.core.data.Offset;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.spatial.function.geohash.StLatFromGeoHash}.
 */
class StLatFromGeoHashTest {

    @ParameterizedTest
    @CsvSource({
        "'ezs42', 42.6",
        "'zzzzzzzzz', 90",
        "'xvrfxvrfxvrfxvr', 30"
    })
    void testLatFromGeoHash(String geohash, double expected) throws SQLException {
        verifyQuery(
            "SELECT ST_LatFromGeoHash(?)",
            statement -> statement.setString(1, geohash),
            rs -> assertThat(rs.getDouble(1)).isCloseTo(expected, Offset.offset(0.1))
        );
    }
}
