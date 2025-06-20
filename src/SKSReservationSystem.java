import db_connection.DatabaseConnection;
import frame.LoginFrame;

import java.sql.*;
import javax.swing.*;

public class SKSReservationSystem {
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check database connection and initialize
        if (initializeDatabase()) {
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(null,
                    "Error: Tidak dapat terhubung ke database!\n" +
                            "Pastikan MySQL server berjalan dan database 'sks_reservation' tersedia.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}