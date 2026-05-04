package cz.miou.h2.mariadb.aggregate;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.aggregate.BitXor}.
 */
class BitXorTest {

    @Test
    void testBitXor() throws SQLException {
        verifyQuery(
            "SELECT BIT_XOR(v) FROM (SELECT 1 v UNION ALL SELECT 2 UNION ALL SELECT 3)",
            rs -> assertThat(rs.getBigDecimal(1)).isEqualByComparingTo(BigDecimal.ZERO)
        );
    }

    @Test
    void testBitXorIgnoresNulls() throws SQLException {
        verifyQuery(
            "SELECT BIT_XOR(v) FROM (SELECT CAST(NULL AS BIGINT) v UNION ALL SELECT 1 UNION ALL SELECT 4)",
            rs -> assertThat(rs.getBigDecimal(1)).isEqualByComparingTo(BigDecimal.valueOf(5))
        );
    }

    @Test
    void testBitXorEmptyResult() throws SQLException {
        verifyQuery(
            "SELECT BIT_XOR(v) FROM (SELECT 1 v WHERE FALSE)",
            rs -> assertThat(rs.getBigDecimal(1)).isEqualByComparingTo(BigDecimal.ZERO)
        );
    }

    @Test
    void testBitXorUnsigned64BitResult() throws SQLException {
        verifyQuery(
            "SELECT BIT_XOR(v) FROM (SELECT -1 v)",
            rs -> assertThat(rs.getBigDecimal(1)).isEqualByComparingTo(new BigDecimal("18446744073709551615"))
        );
    }
}
