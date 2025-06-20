package db_connection;
import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/mahasiswa_sks";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "hansen";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
