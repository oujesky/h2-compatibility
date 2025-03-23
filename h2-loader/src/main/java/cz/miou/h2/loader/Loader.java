package cz.miou.h2.loader;

import cz.miou.h2.api.AggregateDefinition;
import cz.miou.h2.api.FunctionDefinition;
import cz.miou.h2.api.TypeDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Utility class for loading and registering the defined functions, aggregates and type aliases to an H2 database
 * <p>
 * {@link java.util.ServiceLoader} mechanism is utilized to discover instances of {@link cz.miou.h2.api.FunctionDefinition},
 * {@link cz.miou.h2.api.AggregateDefinition} and {@link cz.miou.h2.api.TypeDefinition}.
 */
public class Loader {

    private static final Logger LOG = LoggerFactory.getLogger(Loader.class);

    public static final String PROPERTY_EXCLUDE = "cz.miou.h2.loader.exclude";

    private static final int ERROR_CODE_FUNCTION_ALIAS_ALREADY_EXISTS = 90076;
    private static final int ERROR_CODE_DOMAIN_ALREADY_EXISTS = 90119;

    private Loader() {}

    /**
     * Loads and registers all discovered functions, aggregates and type aliases to an H2 database
     *
     * @param connection JDBC connection to a H2 database
     * @throws SQLException in case any SQL error, except for duplicate aliases
     */
    public static void load(Connection connection) throws SQLException {
        var excludes = loadExcludes();

        try (var statement = connection.createStatement()) {
            registerTypes(statement, excludes);
            registerFunctions(statement, excludes);
            registerAggregates(statement, excludes);
        }
    }

    private static void registerTypes(Statement statement, Set<String> excludes) throws SQLException {
        for (var definition : ServiceLoader.load(TypeDefinition.class)) {

            if (excludes.contains(definition.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Type {} is excluded", definition.getName());
                }
                continue;
            }

            try {
                registerType(statement, definition);
            } catch (SQLException e) {
                if (e.getErrorCode() == ERROR_CODE_DOMAIN_ALREADY_EXISTS) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Type {} already exists", definition.getName());
                    } else {
                        throw e;
                    }
                }
            }
        }
    }

    private static void registerType(Statement statement, TypeDefinition definition) throws SQLException {
        var name = definition.getName();
        var type = definition.getType();

        statement.execute("CREATE DOMAIN IF NOT EXISTS `" + name + "` AS " + type);

        if (LOG.isTraceEnabled()) {
            LOG.trace("Registered type {}", name);
        }
    }

    private static void registerFunctions(Statement statement, Set<String> excludes) throws SQLException {
        for (var definition : ServiceLoader.load(FunctionDefinition.class)) {

            if (excludes.contains(definition.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Function {} is excluded", definition.getName());
                }
                continue;
            }

            try {
                registerFunction(statement, definition);
            } catch (SQLException e) {
                if (e.getErrorCode() == ERROR_CODE_FUNCTION_ALIAS_ALREADY_EXISTS) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Function {} already exists", definition.getName());
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    private static void registerFunction(Statement statement, FunctionDefinition definition) throws SQLException {
        var deterministic = definition.isDeterministic() ? " DETERMINISTIC" : "";
        var name = definition.getName();
        var functionClass = definition.getClass().getName();
        var functionMethod = definition.getMethodName();

        statement.execute("CREATE ALIAS IF NOT EXISTS " + name + deterministic + " FOR \"" + functionClass + "." + functionMethod + "\"");

        if (LOG.isTraceEnabled()) {
            LOG.trace("Registered function {}", name);
        }
    }

    private static void registerAggregates(Statement statement, Set<String> excludes) throws SQLException {
        for (var definition : ServiceLoader.load(AggregateDefinition.class)) {

            if (excludes.contains(definition.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Aggregate {} is excluded", definition.getName());
                }
                continue;
            }

            try {
                registerAggregate(statement, definition);
            } catch (SQLException e) {
                if (e.getErrorCode() == ERROR_CODE_FUNCTION_ALIAS_ALREADY_EXISTS) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Aggregate {} already exists", definition.getName());
                    }
                } else {
                    throw e;
                }
            }
        }
    }

    private static void registerAggregate(Statement statement, AggregateDefinition definition) throws SQLException {
        var name = definition.getName();
        var functionClass = definition.getClass().getName();

        statement.execute("CREATE AGGREGATE IF NOT EXISTS " + name + " FOR \"" + functionClass + "\"");

        if (LOG.isTraceEnabled()) {
            LOG.trace("Registered aggregate {}", name);
        }
    }

    private static Set<String> loadExcludes() {
        var excludes = System.getProperty(PROPERTY_EXCLUDE);
        return excludes != null
            ? Set.of(excludes.split("\\s*,\\s*"))
            : Set.of();
    }

}
