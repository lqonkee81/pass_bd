/*
 * Обработчик базы данных
 *
 * Добавляет или удаляет авторизационные данные из базы пользлвателей
 * */

import java.sql.*;
import java.util.Locale;

public class DataBaseHandler {
    private Connection co;

    DataBaseHandler() {
        String pathDbFile = "DataBase/users.db";

        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:" + pathDbFile);

            String request = " CREATE TABLE IF NOT EXISTS users (\n " +
                    " login TEXT UNIQUE NOT NULL,\n" +
                    " password TEXT NOT NULL,\n" +
                    " phone_number TEXT,\n" +
                    " email TEXT\n" +
                    ");";


            Statement statement = co.createStatement();
            statement.execute(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void addUser(String login,
                                     String password,
                                     String phoneNumber,
                                     String email
    ) throws SQLException {
        String response = "INSERT INTO users (login, password, phone_number, email) " +
                "VALUES('" + login + "', '" + password + "', '" + phoneNumber + "', '" + email + "')";

        Statement statement = co.createStatement();
        statement.executeUpdate(response);
    }

    public synchronized boolean checkUser(String login, String password) throws SQLException {
        String request = "SELECT login, password FROM users WHERE login = ?;')";

        PreparedStatement ps = co.prepareStatement(request);
        ps.setString(1, login);

        ResultSet result = ps.executeQuery();

        String responce_login = result.getString("login");
        String responce_password = result.getString("password");

        try {
            if (responce_login.equals(login) && responce_password.equals(password)) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }
}