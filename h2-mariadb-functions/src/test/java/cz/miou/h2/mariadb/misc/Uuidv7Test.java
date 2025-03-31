package cz.miou.h2.mariadb.misc;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class Uuidv7Test {

    @Test
    void testUuidV7() throws SQLException {
        verifyQuery(
            "SELECT UUIDV7()",
            rs -> assertThat(rs.getString(1))
                .containsPattern("^([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})$")
        );
    }
    
}