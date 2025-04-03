package cz.miou.h2.mariadb.uuid;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class UuidV4Test {

    @Test
    void testUuidV4() throws SQLException {
        verifyQuery(
            "SELECT UUIDV4()",
            rs -> assertThat(rs.getString(1))
                .containsPattern("^([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})$")
        );
    }
    
}