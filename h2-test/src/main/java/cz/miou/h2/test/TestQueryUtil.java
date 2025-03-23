package cz.miou.h2.test;

import cz.miou.h2.loader.Loader;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class TestQueryUtil {

    private TestQueryUtil() {}

    public static void verifyQuery(String mode, String sql, SqlThrowingConsumer<ResultSet> verifier) throws SQLException {
        verifyQuery(mode, sql, statement -> {}, verifier);
    }

    public static void verifyQuery(String mode, String sql, SqlThrowingConsumer<PreparedStatement> binder, SqlThrowingConsumer<ResultSet> verifier) throws SQLException {
        try (var connection = createConnection(mode);
             var statement = connection.prepareStatement(sql)) {

            binder.accept(statement);

            statement.executeQuery();

            try (var rs = statement.executeQuery()) {
                assertThat(rs.next()).isTrue();
                verifier.accept(rs);
            }
        }
    }

    private static Connection createConnection(String mode) throws SQLException {
        var ds = createDataSource(mode);
        var connection = ds.getConnection();

        Loader.load(connection);

        return connection;
    }

    private static DataSource createDataSource(String mode) {
        Locale.setDefault(Locale.US);
        var ds = new JdbcDataSource();
        ds.setURL(String.format("jdbc:h2:mem:;MODE=%s;TIME ZONE=UTC", mode));
        ds.setUser("sa");
        return ds;
    }

    @FunctionalInterface
    public interface SqlThrowingConsumer<T> {
        void accept(T value) throws SQLException;
    }

}
