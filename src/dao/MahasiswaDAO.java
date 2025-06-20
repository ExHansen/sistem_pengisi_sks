package dao;

import db_connection.DatabaseConnection;
import db_connection.Mahasiswa;

import java.sql.*;

public class MahasiswaDAO {
    public boolean login(String nim, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM mahasiswa WHERE nim = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nim);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean register(Mahasiswa mahasiswa) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO mahasiswa (nim, nama, email, password, semester, ipk) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, mahasiswa.getNim());
            stmt.setString(2, mahasiswa.getNama());
            stmt.setString(3, mahasiswa.getEmail());
            stmt.setString(4, mahasiswa.getPassword());
            stmt.setInt(5, mahasiswa.getSemester());
            stmt.setDouble(6, mahasiswa.getIpk());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Mahasiswa getMahasiswa(String nim) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM mahasiswa WHERE nim = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Mahasiswa(
                        rs.getString("nim"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("semester"),
                        rs.getDouble("ipk")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}