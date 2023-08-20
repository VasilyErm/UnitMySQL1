package ru.netology.banklogin.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.openqa.selenium.bidi.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        return (Connection) DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
    }

    @SneakyThrows
    public static DataHelper.VerificationCode getVerificationCode() {
        var codeSQL = "SELECT code FROM auths_codes ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var code = runner.query((java.sql.Connection) conn, codeSQL, new ScalarHandler<String>());
        return new DataHelper.VerificationCode(code);
    }

    @SneakyThrows
    public static void cleanDataBase() {
        var connection = getConn();
        runner.execute((java.sql.Connection) connection, "DELETE FROM auth_codes");
        runner.execute((java.sql.Connection) connection, "DELETE FROM cards_transactions");
        runner.execute((java.sql.Connection) connection, "DELETE FROM cards");
        runner.execute((java.sql.Connection) connection, "DELETE FROM users");
    }
}
