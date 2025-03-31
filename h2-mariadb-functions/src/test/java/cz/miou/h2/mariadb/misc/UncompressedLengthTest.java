package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class UncompressedLengthTest {

    @Test
    void testUncompressedLength() throws SQLException {
        verifyQuery(
            "SELECT UNCOMPRESSED_LENGTH(COMPRESS(REPEAT('a', 30)))",
            rs -> assertThat(rs.getInt(1)).isEqualTo(30)
        );
    }
    
}