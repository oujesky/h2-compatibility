package cz.miou.h2.test;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.sql.Connection;

public class H2ConnectionExtension implements ParameterResolver, BeforeEachCallback, AfterEachCallback {

    private Connection connection;

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(Connection.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return connection;
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        var ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:");
        ds.setUser("sa");

        connection = ds.getConnection();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
