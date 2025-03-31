package cz.miou.h2.mariadb.string;

import cz.miou.h2.api.FunctionDefinition;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <a href="https://mariadb.com/kb/en/load_file/">LOAD_FILE</a>
 */
public class LoadFile implements FunctionDefinition {

    @Override
    public String getName() {
        return "LOAD_FILE";
    }

    @Override
    public String getMethodName() {
        return "loadFile";
    }

    @SuppressWarnings("unused")
    public static String loadFile(Connection connection, String fileName) throws SQLException {
        try (var statement = connection.prepareStatement("SELECT FILE_READ(?)")) {
            statement.setString(1, fileName);
            try (var resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getString(1) : null;
            }
        }
    }

}
