package cz.miou.h2.mariadb.string;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class ToBase64Test {

    @Test
    void testToBase64() throws SQLException {
        verifyQuery(
            "SELECT TO_BASE64(?)",
            st -> st.setString(1, "Maria"),
            rs -> assertThat(rs.getString(1)).isEqualTo("TWFyaWE=")
        );
    }

}