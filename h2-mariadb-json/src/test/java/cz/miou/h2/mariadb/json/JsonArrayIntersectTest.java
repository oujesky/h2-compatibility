package cz.miou.h2.mariadb.json;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class JsonArrayIntersectTest {
    
    @ParameterizedTest
    @CsvSource({
        "'[1,2,3]', '[1,2,3]', '[1,2,3]'",
        "'[1,2,3]', '[1,2,4]', '[1,2]'",
        "'[1,2,3]', '[4,5,6]', ",
        "'[1,2,3,3,3]', '[2,3,2]', '[2,3]'",
        "'[1,2,3]', '1', ",
        "'[1,2,3]', '\"abc\"', ",
        "'[1,2,3]', 'true', ",
    })
    void testJsonArrayIntersect(String arr1, String arr2, String expected) throws SQLException {
        verifyQuery(
            "SELECT JSON_ARRAY_INTERSECT(?, ?)",
            st -> {
                st.setString(1, arr1);
                st.setString(2, arr2);
            },
            rs -> assertThatJson(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}