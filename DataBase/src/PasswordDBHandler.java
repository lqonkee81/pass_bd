/*
 * Отвечает за взимодействие с базой данных
 * конкретного пользователя
 * */


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PasswordDBHandler {
    private Connection connection;

    PasswordDBHandler(String login) {
        String path = "DataBase/users_bd/" + login + ".db";

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);

            String request = " CREATE TABLE IF NOT EXISTS data (\n " +
                    " url TEXT NOT NULL,\n" +
                    " login TEXT NOT NULL,\n" +
                    " password TEXT NOT NULL,\n" +
                    " last_update_time TEXT NOT NULL,\n" +
                    ");";


            Statement statement = connection.createStatement();
            statement.execute(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRow(String url, String login, String password) throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        java.util.Date date = new Date(System.currentTimeMillis());
        String format_date = formatter.format(date); // Уже отформатированное время и дата

        String responce = "INSERT INTO data (url, login, password, last_update_time) " +
                "VALUES ('" + url + "', '" + login + "', '" + password + "', '" + format_date + "')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(responce);
    }
}
