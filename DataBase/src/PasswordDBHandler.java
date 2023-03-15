/*
 * Отвечает за взимодействие с базой данных
 * конкретного пользователя
 * */


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PasswordDBHandler {
    private Connection connection;

    PasswordDBHandler(String login) {
        String path = "DataBase/users_db/" + login + ".db";

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);

            String request = " CREATE TABLE IF NOT EXISTS data (\n " +
                    " url TEXT NOT NULL,\n" +
                    " login TEXT NOT NULL,\n" +
                    " password TEXT NOT NULL,\n" +
                    " last_update_time TEXT NOT NULL\n" +
                    ");";


            Statement statement = connection.createStatement();
            statement.executeUpdate(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRow(String url, String login, String password) throws SQLException {
        /*
         * Добавдение записи в базу данных с паролями пользователя
         * */

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        java.util.Date date = new Date(System.currentTimeMillis());
        String format_date = formatter.format(date); // Уже отформатированное время и дата

        String responce = "INSERT INTO data (url, login, password, last_update_time) " +
                "VALUES ('" + url + "', '" + login + "', '" + password + "', '" + format_date + "')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(responce);
    }

    public void deleteRow(String url) throws SQLException {
        /*
         * Удаление записи из базы данных с паролями пользователя
         * */

        String request = "DELETE FROM data WHERE url = ?;";

        PreparedStatement statement = connection.prepareStatement(request);
        statement.setString(1, url);
        statement.executeUpdate();
    }

    public ArrayList<String> getFullDataBase() throws SQLException {
        /*
         * Считывает всю базу данных пользователя
         * */

        String request = "SELECT * FROM data";

        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(request);

        ArrayList<String> urls = new ArrayList<>();

        while (result.next()) {
            urls.add(result.getString(1));
        }

        return urls;
    }
}
