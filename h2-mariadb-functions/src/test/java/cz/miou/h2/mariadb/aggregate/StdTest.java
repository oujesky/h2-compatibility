package cz.miou.h2.mariadb.aggregate;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link cz.miou.h2.mariadb.aggregate.Std}.
 */
class StdTest {

    @Test
    void testStdIsPopulationStandardDeviation() throws SQLException {
        verifyQuery(
            "SELECT STD(v) FROM (SELECT 2 v UNION ALL SELECT 4 UNION ALL SELECT 4 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 5 UNION ALL SELECT 7 UNION ALL SELECT 9)",
            rs -> assertThat(rs.getDouble(1)).isEqualTo(2.0)
        );
    }

    @Test
    void testStdIgnoresNulls() throws SQLException {
        verifyQuery(
            "SELECT STD(v) FROM (SELECT CAST(NULL AS DOUBLE) v UNION ALL SELECT 1 UNION ALL SELECT 3)",
            rs -> assertThat(rs.getDouble(1)).isEqualTo(1.0)
        );
    }

    @Test
    void testStdAllNulls() throws SQLException {
        verifyQuery(
            "SELECT STD(v) FROM (SELECT CAST(NULL AS DOUBLE) v)",
            rs -> assertThat(rs.getObject(1)).isNull()
        );
    }
}
