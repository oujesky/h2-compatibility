package cz.miou.h2.test;

import cz.miou.h2.test.TestQueryUtil.SqlThrowingConsumer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MariaDbTestQueryUtil {

    private MariaDbTestQueryUtil() {}

    public static void verifyQuery(String sql, SqlThrowingConsumer<ResultSet> verifier) throws SQLException {
        TestQueryUtil.verifyQuery("MariaDB", sql, verifier);
    }

    public static void verifyQuery(String sql, SqlThrowingConsumer<PreparedStatement> binder, SqlThrowingConsumer<ResultSet> verifier) throws SQLException {
        TestQueryUtil.verifyQuery("MariaDB", sql, binder, verifier);
    }

    public static void verifyQueryError(String sql, String expectedMessage) {
        TestQueryUtil.verifyQueryError("MariaDB", sql, expectedMessage);
    }
}
