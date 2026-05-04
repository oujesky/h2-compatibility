package cz.miou.h2.mariadb.misc;

import cz.miou.h2.api.FunctionDefinition;
import org.h2.jdbc.JdbcConnection;

/**
 * <a href="https://mariadb.com/kb/en/connection_id/">CONNECTION_ID</a>
 */
public class ConnectionId implements FunctionDefinition {

    @Override
    public String getName() {
        return "CONNECTION_ID";
    }

    @Override
    public String getMethodName() {
        return "connectionId";
    }

    @SuppressWarnings("unused")
    public static long connectionId(JdbcConnection connection) {
        return Integer.toUnsignedLong(System.identityHashCode(connection.getSession()));
    }
}
