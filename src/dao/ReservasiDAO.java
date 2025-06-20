package dao;
import db_connection.DatabaseConnection;
import db_connection.Reservasi;

import java.sql.*;
import java.util.*;
import java.util.List;

public class ReservasiDAO {
    public boolean addReservasi(Reservasi reservasi) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO reservasi (nim, kode_mk, tanggal_reservasi, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, reservasi.getNim());
            stmt.setString(2, reservasi.getKodeMk());
            stmt.setTimestamp(3, new Timestamp(reservasi.getTanggalReservasi().getTime()));
            stmt.setString(4, reservasi.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservasi> getReservasiByNim(String nim) {
        List<Reservasi> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM reservasi WHERE nim = ? ORDER BY tanggal_reservasi DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reservasi reservasi = new Reservasi(
                        rs.getString("nim"),
                        rs.getString("kode_mk")
                );
                reservasi.setId(rs.getInt("id"));
                reservasi.setStatus(rs.getString("status"));
                list.add(reservasi);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isAlreadyReserved(String nim, String kodeMk) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM reservasi WHERE nim = ? AND kode_mk = ? AND status = 'AKTIF'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nim);
            stmt.setString(2, kodeMk);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getTotalSksReserved(String nim) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT SUM(mk.sks) as total FROM reservasi r " +
                    "JOIN mata_kuliah mk ON r.kode_mk = mk.kode_mk " +
                    "WHERE r.nim = ? AND r.status = 'AKTIF'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean cancelReservasi(String nim, String kodeMk) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE reservasi SET status = 'DIBATALKAN' WHERE nim = ? AND kode_mk = ? AND status = 'AKTIF'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nim);
            stmt.setString(2, kodeMk);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
