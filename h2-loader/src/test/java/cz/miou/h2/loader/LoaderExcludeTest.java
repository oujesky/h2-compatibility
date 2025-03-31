package cz.miou.h2.loader;

import cz.miou.h2.test.H2ConnectionExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;

import static cz.miou.h2.loader.Loader.PROPERTY_EXCLUDE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(H2ConnectionExtension.class)
class LoaderExcludeTest {

    private String originalExclude;

    @BeforeEach
    void setUp() {
        originalExclude = System.getProperty(PROPERTY_EXCLUDE);
    }

    @AfterEach
    void tearDown() {
        if (originalExclude == null) {
            System.clearProperty(PROPERTY_EXCLUDE);
        } else {
            System.setProperty(PROPERTY_EXCLUDE, originalExclude);
        }
    }

    @Test
    void testExcludeType(Connection connection) throws SQLException {
        System.setProperty(PROPERTY_EXCLUDE, "TEST_TYPE");

        Loader.load(connection);

        var statement = connection.createStatement();
        assertThatThrownBy(() -> statement.execute("CREATE TABLE test_table (test_column TEST_TYPE)"))
            .isInstanceOf(SQLException.class)
            .hasMessageStartingWith("Unknown data type: \"TEST_TYPE\";");
    }

    @Test
    void testExcludeFunction(Connection connection) throws SQLException {
        System.setProperty(PROPERTY_EXCLUDE, "TEST_FUNCTION");

        Loader.load(connection);

        var statement = connection.createStatement();
        assertThatThrownBy(() -> statement.execute("SELECT TEST_FUNCTION('input')"))
            .isInstanceOf(SQLException.class)
            .hasMessageStartingWith("Function \"TEST_FUNCTION\" not found;");
    }

    @Test
    void testExcludeFunctionAlias(Connection connection) throws SQLException {
        System.setProperty(PROPERTY_EXCLUDE, "TEST_FUNCTION_ALIAS");

        Loader.load(connection);

        var statement = connection.createStatement();
        assertThatThrownBy(() -> statement.execute("SELECT TEST_FUNCTION_ALIAS('input')"))
            .isInstanceOf(SQLException.class)
            .hasMessageStartingWith("Function \"TEST_FUNCTION_ALIAS\" not found;");
    }

    @Test
    void testExcludeAggregate(Connection connection) throws SQLException {
        System.setProperty(PROPERTY_EXCLUDE, "TEST_AGGREGATE");

        Loader.load(connection);

        var statement = connection.createStatement();
        statement.execute("CREATE TABLE test_aggregate_table(test_column VARCHAR(255))");
        statement.execute("INSERT INTO test_aggregate_table (test_column) VALUES ('test1'), ('test2')");

        assertThatThrownBy(() -> statement.execute("SELECT TEST_AGGREGATE(test_column) FROM test_aggregate_table"))
            .isInstanceOf(SQLException.class)
            .hasMessageStartingWith("Function \"TEST_AGGREGATE\" not found;");
    }

}
