package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class FromDaysTest {

    @ParameterizedTest
    @CsvSource({
        "0, '0000-00-00'",
        "365, '0000-00-00'",
        "366, '0001-01-01'",
        "719528, '1970-01-01'",
        "730669, '2000-07-03'",
        "728779, '1995-05-01'",
        "3652424, '9999-12-31'"
    })
    void testFromDays(int days, String expected) throws SQLException {
        verifyQuery(
            "SELECT FROM_DAYS(?)",
            st -> st.setInt(1, days),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
}