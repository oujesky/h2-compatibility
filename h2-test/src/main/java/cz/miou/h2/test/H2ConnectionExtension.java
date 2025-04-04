package cz.miou.h2.test;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.UUID;

public class H2ConnectionExtension implements ParameterResolver, BeforeEachCallback, AfterEachCallback {

    private JdbcDataSource dataSource;
    private Connection connection;

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(Connection.class)
               || parameterContext.getParameter().getType().equals(DataSource.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if (parameterContext.getParameter().getType().equals(Connection.class)) {
            return connection;
        }

        if (parameterContext.getParameter().getType().equals(DataSource.class)) {
            return dataSource;
        }

        throw new IllegalStateException("Unsupported parameter type: " + parameterContext.getParameter().getType());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:" + UUID.randomUUID());
        dataSource.setUser("sa");

        connection = dataSource.getConnection();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
