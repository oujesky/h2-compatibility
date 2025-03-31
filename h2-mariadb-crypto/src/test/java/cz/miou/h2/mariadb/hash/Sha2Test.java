package cz.miou.h2.mariadb.hash;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static cz.miou.h2.test.MariaDbTestQueryUtil.verifyQuery;
import static org.assertj.core.api.Assertions.assertThat;

class Sha2Test {

    @ParameterizedTest
    @CsvSource({
        "'Maria', 224, '6cc67add32286412efcab9d0e1675a43a5c2ef3cec8879f81516ff83'",
        "'Maria', 256, '9ff18ebe7449349f358e3af0b57cf7a032c1c6b2272cb2656ff85eb112232f16'",
        "'Maria', 0, '9ff18ebe7449349f358e3af0b57cf7a032c1c6b2272cb2656ff85eb112232f16'",
        "'Maria', 384, '5f74786c5d35896b9810fa84a465991988929eb06b87f28d55ffd24b6cd7ab71a4f536733f39f0e2cf92c3a7d20816c5'",
        "'Maria', 512, '88a4948f593a1778201c859c6c18538769e89cadfb7547a9f2869990383273410a79cf98e171e4618fbeb91c8355e96d9f25cb8f88e40954e64e4780aa736875'",
    })
    void testSha2(String str, int hashLen, String expected) throws SQLException {
        verifyQuery(
            "SELECT SHA2(?, ?)",
            st -> {
                st.setString(1, str);
                st.setInt(2, hashLen);
            },
            rs -> assertThat(rs.getString(1)).isEqualTo(expected)
        );
    }

}