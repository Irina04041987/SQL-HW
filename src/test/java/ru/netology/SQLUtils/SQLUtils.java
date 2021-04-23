package ru.netology.SQLUtils;

import lombok.val;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLUtils {
    public static Connection getConnection() throws SQLException {
        final Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/db", "user", "pass");
        return connection;
    }

    public static String getVerificationCode(String login) throws SQLException {

        String code = null;
        final char dm = (char) 34;
        val authCode = "select  code from auth_codes" +
                "       inner join users on auth_codes.user_id = users.id" +
                "       where login =" + dm + login + dm +
                "       order by created desc limit 1";

        try (val conn = getConnection();
             val codeStmt = conn.createStatement();
        ) {

            try (val rs = codeStmt.executeQuery(authCode)) {
                if (rs.next()) {
                    code = rs.getString("code");
                }
            }
        }
        return code;
    }


    public String getStatusFromDb(String login) throws SQLException {
        final char dm = (char) 34;
        String statusSQL = "SELECT status FROM users WHERE login = " + dm + login + dm;
        String status = null;
        try (val conn = getConnection();
             val statusStmt = conn.createStatement();) {
            try (val rs = statusStmt.executeQuery(statusSQL)) {
                if (rs.next()) {
                    status = rs.getString("status");
                }
            }
        }
        return status;
    }

    public static void cleanDb() throws SQLException {
        String deleteCards = "DELETE FROM cards; ";
        String deleteAuthCodes = "DELETE FROM auth_codes; ";
        String deleteUsers = "DELETE FROM users; ";
        try (val conn = SQLUtils.getConnection();
             val deleteCardsStmt = conn.createStatement();
             val deleteAuthCodesStmt = conn.createStatement();
             val deleteUsersStmt = conn.createStatement();
        ) {
            deleteCardsStmt.executeUpdate(deleteCards);
            deleteAuthCodesStmt.executeUpdate(deleteAuthCodes);
            deleteUsersStmt.executeUpdate(deleteUsers);
        }
    }
}
