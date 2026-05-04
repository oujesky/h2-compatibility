package cz.miou.h2.mariadb.spatial.type;

import cz.miou.h2.loader.Loader;
import org.locationtech.jts.geom.Geometry;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class GeometryTestUtil {

    static <T extends Geometry> void verifyGeometry(Connection connection, String type, String value, Class<T> geometryType, Consumer<T> verifier) throws SQLException {
        Loader.load(connection);

        var statement = connection.createStatement();
        statement.execute(String.format("CREATE TABLE test (g %s)", type));
        statement.execute(String.format("INSERT INTO test VALUES ('%s')", value));
        statement.execute("SELECT * FROM test");

        var rs = statement.getResultSet();
        assertThat(rs.next()).isTrue();
        assertThat(rs.getObject(1))
            .isInstanceOfSatisfying(geometryType, verifier);
    }

}
