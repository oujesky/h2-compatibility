package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class FindInSetTest {
    
    @ParameterizedTest
    @CsvSource({
        "'a', 'a,b,c,d', 1",
        "'b', 'a,b,c,d', 2",
        "'c', 'a,b,c,d', 3",
        "'d', 'a,b,c,d', 4",
        "'e', 'a,b,c,d', 0",
        "'a', 'a,a,a,a', 1",
    })
    void testFindInSet(String pattern, String strList, int expected) throws SQLException {
        verifyQuery(
            "SELECT FIND_IN_SET(?, ?)",
            st -> {
                st.setString(1, pattern);
                st.setString(2, strList);
            },
            rs -> assertThat(rs.getInt(1)).isEqualTo(expected)
        );
    }
    
}