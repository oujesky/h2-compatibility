package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class ConvertTimeZoneTest {

    @ParameterizedTest
    @CsvSource({
        "'2016-01-01 12:00:00', '+00:00', '+10:00', '2016-01-01 22:00:00'",
        "'2016-01-01 12:00:00', 'GMT', 'Africa/Johannesburg', '2016-01-01 14:00:00'",
        "'1969-12-31 22:00:00', '+00:00', '+10:00', '1969-12-31 22:00:00'"
    })
    void testConvertTimeZone(String date, String from, String to, String expected) throws SQLException {
        verifyQuery(
            "SELECT CONVERT_TZ(?, ?, ?)",
            st -> {
                st.setString(1, date);
                st.setString(2, from);
                st.setString(3, to);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}