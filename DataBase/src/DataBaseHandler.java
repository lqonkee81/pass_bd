import java.sql.*;

public class DataBaseHandler {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection co = DriverManager.getConnection("jdbc:sqlite:../db.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}