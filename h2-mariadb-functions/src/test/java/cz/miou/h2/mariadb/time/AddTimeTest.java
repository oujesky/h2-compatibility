package cz.miou.h2.mariadb.time;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class AddTimeTest {

    @ParameterizedTest
    @CsvSource({
        "'2007-12-31 23:59:59.999999', '1 1:1:1.000002', '2008-01-02 01:01:01.000001'",
        "'01:00:00.999999', '02:00:00.999998', '03:00:01.999997'",
        "'01:00:00', '02:00:00', '03:00:00'",
    })
    void testAddTime(String expr1, String expr2, String expected) throws SQLException {
        verifyQuery(
            "SELECT ADDTIME(?, ?)",
            st -> {
                st.setString(1, expr1);
                st.setString(2, expr2);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}