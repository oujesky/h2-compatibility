package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class OrdTest {

    @ParameterizedTest
    @CsvSource({
        "'', 0",
        "'2', 50",
        "'č', 50317",
        "'abc', 97"
    })
    void testOrd(String input, int expected) throws SQLException {
        verifyQuery(
            "SELECT ORD(?)",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );

    }



}