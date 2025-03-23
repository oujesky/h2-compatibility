package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class TimeFormatTest {

    @ParameterizedTest
    @CsvSource({
        "'16:00:00', '%H %k %h %I %l', '16 16 04 04 4'",
        "'4:00:00', '%H %k %h %I %l', '04 4 04 04 4'",
    })
    void testTimeFormat(String input, String format, String expected) throws SQLException {
        verifyQuery(
            "SELECT TIME_FORMAT(?, ?)",
            st -> {
                st.setString(1, input);
                st.setString(2, format);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}