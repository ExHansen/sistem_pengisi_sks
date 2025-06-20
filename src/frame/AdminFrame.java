package frame;

import dao.MahasiswaDAO;
import dao.MatakuliahDAO;
import db_connection.DatabaseConnection;
import db_connection.Mahasiswa;
import db_connection.Matakuliah;

import java.sql.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminFrame extends JFrame {
    private MatakuliahDAO mataKuliahDAO;
    private MahasiswaDAO mahasiswaDAO;
    private JTabbedPane tabbedPane;

    public AdminFrame() {
        this.mataKuliahDAO = new MatakuliahDAO();
        this.mahasiswaDAO = new MahasiswaDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Admin Panel - Sistem Reservasi SKS");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Kelola Mata Kuliah", createMataKuliahAdminPanel());
        tabbedPane.addTab("Kelola Mahasiswa", createMahasiswaAdminPanel());

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.addActionListener(e -> logout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(logoutButton);

        add(topPanel, BorderLayout.NORTH);

        add(tabbedPane, BorderLayout.CENTER);

        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    private JPanel createMataKuliahAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Kode MK", "Nama", "SKS", "Semester", "Dosen", "Hari", "Waktu", "Kuota", "Terisi"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        // Load data
        refreshMKTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Tambah MK");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> showAddMKDialog(model));
        refreshButton.addActionListener(e -> refreshMKTable(model));

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMahasiswaAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"NIM", "Nama", "Email", "Semester", "IPK", "Max SKS"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        // Load data
        refreshMahasiswaTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshMahasiswaTable(model));

        buttonPanel.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshMKTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<Matakuliah> list = mataKuliahDAO.getAllMataKuliah();
        for (Matakuliah mk : list) {
            model.addRow(new Object[]{
                    mk.getKodeMk(), mk.getNamaMk(), mk.getSks(), mk.getSemester(),
                    mk.getDosen(), mk.getHari(), mk.getWaktu(), mk.getKuota(), mk.getTerisi()
            });
        }
    }

    private void refreshMahasiswaTable(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM mahasiswa ORDER BY nim";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Mahasiswa mhs = new Mahasiswa(
                        rs.getString("nim"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("semester"),
                        rs.getDouble("ipk")
                );

                model.addRow(new Object[]{
                        mhs.getNim(), mhs.getNama(), mhs.getEmail(),
                        mhs.getSemester(), mhs.getIpk(), mhs.getMaxSks()
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddMKDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Tambah Mata Kuliah", true);
        dialog.setLayout(new GridLayout(9, 2, 10, 10));

        JTextField kodeField = new JTextField();
        JTextField namaField = new JTextField();
        JSpinner sksSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 6, 1));
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        JTextField dosenField = new JTextField();
        JComboBox<String> hariCombo = new JComboBox<>(new String[]{
                "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"
        });
        JTextField waktuField = new JTextField();
        JSpinner kuotaSpinner = new JSpinner(new SpinnerNumberModel(30, 1, 100, 1));

        dialog.add(new JLabel("Kode MK:"));
        dialog.add(kodeField);
        dialog.add(new JLabel("Nama MK:"));
        dialog.add(namaField);
        dialog.add(new JLabel("SKS:"));
        dialog.add(sksSpinner);
        dialog.add(new JLabel("Semester:"));
        dialog.add(semesterSpinner);
        dialog.add(new JLabel("Dosen:"));
        dialog.add(dosenField);
        dialog.add(new JLabel("Hari:"));
        dialog.add(hariCombo);
        dialog.add(new JLabel("Waktu:"));
        dialog.add(waktuField);
        dialog.add(new JLabel("Kuota:"));
        dialog.add(kuotaSpinner);

        JButton saveButton = new JButton("Simpan");
        saveButton.addActionListener(e -> {
            if (addMataKuliah(
                    kodeField.getText(),
                    namaField.getText(),
                    (Integer) sksSpinner.getValue(),
                    (Integer) semesterSpinner.getValue(),
                    dosenField.getText(),
                    (String) hariCombo.getSelectedItem(),
                    waktuField.getText(),
                    (Integer) kuotaSpinner.getValue()
            )) {
                refreshMKTable(model);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil ditambah!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambah mata kuliah!");
            }
        });

        JButton cancelButton = new JButton("Batal");
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private boolean addMataKuliah(String kode, String nama, int sks, int semester,
                                  String dosen, String hari, String waktu, int kuota) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO mata_kuliah (kode_mk, nama_mk, sks, semester, dosen, hari, waktu, kuota, terisi) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kode);
            stmt.setString(2, nama);
            stmt.setInt(3, sks);
            stmt.setInt(4, semester);
            stmt.setString(5, dosen);
            stmt.setString(6, hari);
            stmt.setString(7, waktu);
            stmt.setInt(8, kuota);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}

