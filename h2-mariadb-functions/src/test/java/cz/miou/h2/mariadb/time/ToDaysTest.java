package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class ToDaysTest {

    @ParameterizedTest
    @CsvSource({
        "'0001-01-01', 366",
        "'1970-01-01', 719528",
        "'2000-07-03', 730669",
        "'9999-12-31', 3652424",
        "'2007-10-07', 733321",
    })
    void testToDays(String date, int expected) throws SQLException {
        verifyQuery(
            "SELECT TO_DAYS(?)",
            st -> st.setString(1, date),
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "950501, 728779",
        "690501, 755808",
        "700501, 719648",
        "19950501, 728779",
        "20690501, 755808",
        "19700501, 719648",
    })
    void testToDaysFromNumber(int input, int expected) throws SQLException {
        verifyQuery(
            "SELECT TO_DAYS(?)",
            st -> st.setInt(1, input),
            rs -> assertThat(rs.getLong(1)).isEqualTo(expected)
        );
    }
}