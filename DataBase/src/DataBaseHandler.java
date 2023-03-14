/*
 * Обработчик базы данных
 *
 * Добавляет или удаляет авторизационные данные из базы пользлвателей
 * */

import java.sql.*;

public class DataBaseHandler {

    private Connection co;
    private String pathDbFile;

    DataBaseHandler(String pathDbFile) {
        this.pathDbFile = pathDbFile;

        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:" + pathDbFile);

            String request = " CREATE TABLE IF NOT EXISTS users (\n " +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    " loggin TEXT NOT NULL,\n" +
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

    public void addUser(String loggin,
                        String password,
                        String phoneNumber,
                        String email
    ) {

    }
}