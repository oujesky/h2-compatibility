package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class UncompressTest {
    
    @Test
    void testUncompress() throws SQLException {
        var input = "abcd abcd abcd";
        verifyQuery(
            "SELECT UNCOMPRESS(COMPRESS(?))",
            st -> st.setString(1, input),
            rs -> assertThat(rs.getString(1)).isEqualTo(input)
        );
    }
    
}