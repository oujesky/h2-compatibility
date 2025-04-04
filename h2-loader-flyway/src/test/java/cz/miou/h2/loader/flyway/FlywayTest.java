package cz.miou.h2.loader.flyway;

import cz.miou.h2.test.H2ConnectionExtension;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(H2ConnectionExtension.class)
class FlywayTest {

    @Test
    void testFlywayMigration(DataSource dataSource, Connection connection) throws SQLException {
        var flyway = Flyway.configure()
            .dataSource(dataSource)
            .load();

        flyway.migrate();

        var statement = connection.createStatement();
        statement.execute("SELECT TEST_FUNCTION('input')");

        var rs = statement.getResultSet();
        assertThat(rs.next()).isTrue();
        assertThat(rs.getString(1)).isEqualTo("TEST:input");
    }

}
