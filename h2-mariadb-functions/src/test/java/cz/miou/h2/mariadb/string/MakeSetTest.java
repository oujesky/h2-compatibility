package cz.miou.h2.mariadb.string;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class MakeSetTest {
    
    @ParameterizedTest
    @CsvSource(quoteCharacter = '|', value = {
        "0, |'a','b','c'|, ||",
        "1, |'a','b','c'|, |a|",
        "2, |'a','b','c'|, |b|",
        "3, |'a','b','c'|, |a,b|",
        "4, |'a','b','c'|, |c|",
        "5, |'a','b','c'|, |a,c|",
        "6, |'a','b','c'|, |b,c|",
        "7, |'a','b','c'|, |a,b,c|",
        "8, |'a','b','c'|, ||",
        "5, |'hello','nice','world'|, |hello,world|",
        "5, |'hello','nice',NULL,'world'|, |hello|",
        "-1, |'a','b','c'|, |a,b,c|",
    })
    void testMakeSet(int bits, String values, String expected) throws SQLException {

        verifyQuery(
            String.format("SELECT MAKE_SET(?, %s)", values),
            st -> st.setInt(1, bits),
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }
    
}