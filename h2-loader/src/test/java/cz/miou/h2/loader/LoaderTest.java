package cz.miou.h2.loader;

import cz.miou.h2.test.H2ConnectionExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(H2ConnectionExtension.class)
class LoaderTest {

    @Test
    void testTypeDefinition(Connection connection) throws SQLException {
        Loader.load(connection);

        var statement = connection.createStatement();
        statement.execute("CREATE TABLE test_table (test_column TEST_TYPE)");
        statement.execute("INSERT INTO test_table VALUES (123)");
        statement.execute("SELECT * FROM test_table");

        var rs = statement.getResultSet();
        assertThat(rs.next()).isTrue();
        assertThat(rs.getInt(1)).isEqualTo(123);
    }

    @Test
    void testFunctionDefinition(Connection connection) throws SQLException {
        Loader.load(connection);

        var statement = connection.createStatement();
        statement.execute("SELECT TEST_FUNCTION('input')");

        var rs = statement.getResultSet();
        assertThat(rs.next()).isTrue();
        assertThat(rs.getString(1)).isEqualTo("TEST:input");

        statement.execute("SELECT TEST_FUNCTION('input', 123)");

        rs = statement.getResultSet();
        assertThat(rs.next()).isTrue();
        assertThat(rs.getString(1)).isEqualTo("TEST:input:123");
    }

    @Test
    void testAggregateDefinition(Connection connection) throws SQLException {
        Loader.load(connection);

        var statement = connection.createStatement();
        statement.execute("CREATE TABLE test_aggregate_table(test_column VARCHAR(255))");
        statement.execute("INSERT INTO test_aggregate_table (test_column) VALUES ('test1'), ('test2')");
        statement.execute("SELECT TEST_AGGREGATE(test_column) FROM test_aggregate_table");

        var rs = statement.getResultSet();
        assertThat(rs.next()).isTrue();
        assertThat(rs.getString(1)).isEqualTo("test1,test2");
    }

}