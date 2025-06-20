package dao;

import java.sql.Connection;
import java.sql.SQLException;

import db_connection.DatabaseConnection;
import db_connection.Matakuliah;

import java.sql.*;
import java.util.*;
import java.util.List;

public class MatakuliahDAO {
    public List<Matakuliah> getAllMataKuliah() {
        List<Matakuliah> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM mata_kuliah ORDER BY semester, kode_mk";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Matakuliah mk = new Matakuliah(
                        rs.getString("kode_mk"),
                        rs.getString("nama_mk"),
                        rs.getInt("sks"),
                        rs.getInt("semester"),
                        rs.getString("dosen"),
                        rs.getString("hari"),
                        rs.getString("waktu"),
                        rs.getInt("kuota")
                );
                mk.setTerisi(rs.getInt("terisi"));
                list.add(mk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Matakuliah getMataKuliah(String kodeMk) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM mata_kuliah WHERE kode_mk = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kodeMk);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Matakuliah mk = new Matakuliah(
                        rs.getString("kode_mk"),
                        rs.getString("nama_mk"),
                        rs.getInt("sks"),
                        rs.getInt("semester"),
                        rs.getString("dosen"),
                        rs.getString("hari"),
                        rs.getString("waktu"),
                        rs.getInt("kuota")
                );
                mk.setTerisi(rs.getInt("terisi"));
                return mk;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateTerisi(String kodeMk, int increment) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE mata_kuliah SET terisi = terisi + ? WHERE kode_mk = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, increment);
            stmt.setString(2, kodeMk);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}