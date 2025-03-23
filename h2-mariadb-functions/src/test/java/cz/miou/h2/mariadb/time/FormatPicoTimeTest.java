package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class FormatPicoTimeTest {

    @ParameterizedTest
    @CsvSource({
        "43, '43 ps'",
        "4321, '4.32 ns'",
        "43211234, '43.21 us'",
        "432112344321, '432.11 ms'",
        "43211234432123, '43.21 s'",
        "432112344321234, '7.20 min'",
        "4321123443212345, '1.20 h'",
        "432112344321234545, '5.00 d'",
    })
    void testFormatPicoTime(long input, String expected) throws SQLException {
        verifyQuery(
            "SELECT FORMAT_PICO_TIME(?)",
            st -> st.setLong(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }


}