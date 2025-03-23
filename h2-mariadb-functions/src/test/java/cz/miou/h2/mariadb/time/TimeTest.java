package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class TimeTest {

    @ParameterizedTest
    @CsvSource({
        "'2003-12-31 01:02:03', '01:02:03'",
        "'2003-12-31 01:02:03.000123', '01:02:03.000123'"
    })
    void testTime(String input, String expected) throws SQLException {
        verifyQuery(
            "SELECT TIME(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}