/*
 * Обработчик базы данных
 *
 * Добавляет или удаляет авторизационные данные из базы пользлвателей
 * */

import java.sql.*;

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
}