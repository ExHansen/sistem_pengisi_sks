package frame;

import dao.MahasiswaDAO;
import dao.MatakuliahDAO;
import db_connection.DatabaseConnection;
import db_connection.Mahasiswa;
import dao.ReservasiDAO;
import db_connection.Matakuliah;
import db_connection.Reservasi;

import java.sql.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Container;
import java.text.SimpleDateFormat;

public class MainFrame extends JFrame {
    private String currentNim;
    private Mahasiswa currentMahasiswa;
    private MahasiswaDAO mahasiswaDAO;
    private MatakuliahDAO mataKuliahDAO;
    private ReservasiDAO reservasiDAO;
    private JTabbedPane tabbedPane;

    public MainFrame(String nim) {
        this.currentNim = nim;
        this.mahasiswaDAO = new MahasiswaDAO();
        this.mataKuliahDAO = new MatakuliahDAO();
        this.reservasiDAO = new ReservasiDAO();
        this.currentMahasiswa = mahasiswaDAO.getMahasiswa(nim);

        initComponents();
    }

    private void initComponents() {
        setTitle("Sistem Reservasi SKS - " + currentMahasiswa.getNama());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("SISTEM RESERVASI SKS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.addActionListener(e -> logout());

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Profil", createProfilPanel());
        tabbedPane.addTab("Mata Kuliah", createMataKuliahPanel());
        tabbedPane.addTab("Reservasi", createReservasiPanel());
        tabbedPane.addTab("Riwayat", createRiwayatPanel());

        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel createProfilPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informasi Mahasiswa"));
        infoPanel.setPreferredSize(new Dimension(400, 200));

        infoPanel.add(new JLabel("NIM:"));
        infoPanel.add(new JLabel(currentMahasiswa.getNim()));

        infoPanel.add(new JLabel("Nama:"));
        infoPanel.add(new JLabel(currentMahasiswa.getNama()));

        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(new JLabel(currentMahasiswa.getEmail()));

        infoPanel.add(new JLabel("Semester:"));
        infoPanel.add(new JLabel(String.valueOf(currentMahasiswa.getSemester())));

        infoPanel.add(new JLabel("IPK:"));
        infoPanel.add(new JLabel(String.valueOf(currentMahasiswa.getIpk())));

        infoPanel.add(new JLabel("Maksimal SKS:"));
        JLabel maxSksLabel = new JLabel(String.valueOf(currentMahasiswa.getMaxSks()));
        maxSksLabel.setForeground(new Color(52, 152, 219));
        maxSksLabel.setFont(maxSksLabel.getFont().deriveFont(Font.BOLD));
        infoPanel.add(maxSksLabel);

        panel.add(infoPanel, gbc);
        return panel;
    }

    private JPanel createMataKuliahPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table
        String[] columns = {"Kode MK", "Nama Mata Kuliah", "SKS", "Semester", "Dosen", "Hari", "Waktu", "Kuota", "Terisi", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Load data
        List<Matakuliah> mataKuliahList = mataKuliahDAO.getAllMataKuliah();
        for (Matakuliah mk : mataKuliahList) {
            String status = mk.getTerisi() >= mk.getKuota() ? "PENUH" : "TERSEDIA";
            model.addRow(new Object[]{
                    mk.getKodeMk(), mk.getNamaMk(), mk.getSks(), mk.getSemester(),
                    mk.getDosen(), mk.getHari(), mk.getWaktu(), mk.getKuota(),
                    mk.getTerisi(), status
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton pilihButton = new JButton("Pilih Mata Kuliah");
        pilihButton.setBackground(new Color(46, 204, 113));
        pilihButton.setForeground(Color.BLACK);
        pilihButton.addActionListener(e -> pilihMataKuliah(table));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshMataKuliah(model));

        buttonPanel.add(pilihButton);
        buttonPanel.add(refreshButton);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReservasiPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informasi SKS"));

        int totalSks = reservasiDAO.getTotalSksReserved(currentNim);

        JLabel totalLabel = new JLabel("Total SKS: " + totalSks, JLabel.CENTER);
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 16f));

        JLabel maxLabel = new JLabel("Maksimal SKS: " + currentMahasiswa.getMaxSks(), JLabel.CENTER);
        maxLabel.setFont(maxLabel.getFont().deriveFont(Font.BOLD, 16f));

        JLabel sisaLabel = new JLabel("Sisa SKS: " + (currentMahasiswa.getMaxSks() - totalSks), JLabel.CENTER);
        sisaLabel.setFont(sisaLabel.getFont().deriveFont(Font.BOLD, 16f));
        sisaLabel.setForeground(totalSks >= currentMahasiswa.getMaxSks() ? Color.RED : new Color(46, 204, 113));

        infoPanel.add(totalLabel);
        infoPanel.add(maxLabel);
        infoPanel.add(sisaLabel);

        // Table
        String[] columns = {"Kode MK", "Nama Mata Kuliah", "SKS", "Dosen", "Jadwal", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        loadReservasiData(model);

        JScrollPane scrollPane = new JScrollPane(table);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton batalButton = new JButton("Batalkan Reservasi");
        batalButton.setBackground(new Color(231, 76, 60));
        batalButton.setForeground(Color.BLACK);
        batalButton.addActionListener(e -> batalkanReservasi(table, model));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            loadReservasiData(model);
            updateInfoPanel(infoPanel);
        });

        buttonPanel.add(batalButton);
        buttonPanel.add(refreshButton);

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRiwayatPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Kode MK", "Nama Mata Kuliah", "SKS", "Tanggal Reservasi", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        loadRiwayatData(model);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadRiwayatData(model));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void pilihMataKuliah(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah terlebih dahulu!");
            return;
        }

        String kodeMk = (String) table.getValueAt(selectedRow, 0);
        String status = (String) table.getValueAt(selectedRow, 9);

        if ("PENUH".equals(status)) {
            JOptionPane.showMessageDialog(this, "Mata kuliah sudah penuh!");
            return;
        }

        // Check if already reserved
        if (reservasiDAO.isAlreadyReserved(currentNim, kodeMk)) {
            JOptionPane.showMessageDialog(this, "Anda sudah memilih mata kuliah ini!");
            return;
        }

        // Check SKS limit
        Matakuliah mk = mataKuliahDAO.getMataKuliah(kodeMk);
        int currentSks = reservasiDAO.getTotalSksReserved(currentNim);

        if (currentSks + mk.getSks() > currentMahasiswa.getMaxSks()) {
            JOptionPane.showMessageDialog(this,
                    "SKS melebihi batas maksimal!\n" +
                            "SKS saat ini: " + currentSks + "\n" +
                            "SKS mata kuliah: " + mk.getSks() + "\n" +
                            "Maksimal SKS: " + currentMahasiswa.getMaxSks());
            return;
        }

        // Confirm reservation
        int confirm = JOptionPane.showConfirmDialog(this,
                "Pilih mata kuliah:\n" +
                        "Kode: " + mk.getKodeMk() + "\n" +
                        "Nama: " + mk.getNamaMk() + "\n" +
                        "SKS: " + mk.getSks() + "\n" +
                        "Dosen: " + mk.getDosen() + "\n" +
                        "Jadwal: " + mk.getHari() + ", " + mk.getWaktu(),
                "Konfirmasi Pilihan",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Reservasi reservasi = new Reservasi(currentNim, kodeMk);

            if (reservasiDAO.addReservasi(reservasi)) {
                mataKuliahDAO.updateTerisi(kodeMk, 1);
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil dipilih!");

                // Refresh tables
                DefaultTableModel mkModel = (DefaultTableModel) table.getModel();
                refreshMataKuliah(mkModel);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memilih mata kuliah!");
            }
        }
    }

    private void refreshMataKuliah(DefaultTableModel model) {
        model.setRowCount(0);
        List<Matakuliah> mataKuliahList = mataKuliahDAO.getAllMataKuliah();
        for (Matakuliah mk : mataKuliahList) {
            String status = mk.getTerisi() >= mk.getKuota() ? "PENUH" : "TERSEDIA";
            model.addRow(new Object[]{
                    mk.getKodeMk(), mk.getNamaMk(), mk.getSks(), mk.getSemester(),
                    mk.getDosen(), mk.getHari(), mk.getWaktu(), mk.getKuota(),
                    mk.getTerisi(), status
            });
        }
    }

    private void loadReservasiData(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT r.kode_mk, mk.nama_mk, mk.sks, mk.dosen, " +
                    "CONCAT(mk.hari, ', ', mk.waktu) as jadwal, r.status " +
                    "FROM reservasi r " +
                    "JOIN mata_kuliah mk ON r.kode_mk = mk.kode_mk " +
                    "WHERE r.nim = ? AND r.status = 'AKTIF' " +
                    "ORDER BY mk.hari, mk.waktu";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentNim);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("kode_mk"),
                        rs.getString("nama_mk"),
                        rs.getInt("sks"),
                        rs.getString("dosen"),
                        rs.getString("jadwal"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadRiwayatData(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT r.kode_mk, mk.nama_mk, mk.sks, r.tanggal_reservasi, r.status " +
                    "FROM reservasi r " +
                    "JOIN mata_kuliah mk ON r.kode_mk = mk.kode_mk " +
                    "WHERE r.nim = ? " +
                    "ORDER BY r.tanggal_reservasi DESC";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, currentNim);
            ResultSet rs = stmt.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("kode_mk"),
                        rs.getString("nama_mk"),
                        rs.getInt("sks"),
                        sdf.format(rs.getTimestamp("tanggal_reservasi")),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void batalkanReservasi(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih reservasi yang akan dibatalkan!");
            return;
        }

        String kodeMk = (String) table.getValueAt(selectedRow, 0);
        String namaMk = (String) table.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Batalkan reservasi mata kuliah:\n" + kodeMk + " - " + namaMk + "?",
                "Konfirmasi Pembatalan",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (reservasiDAO.cancelReservasi(currentNim, kodeMk)) {
                mataKuliahDAO.updateTerisi(kodeMk, -1);
                JOptionPane.showMessageDialog(this, "Reservasi berhasil dibatalkan!");
                loadReservasiData(model);
                updateInfoPanel((JPanel) ((Container) tabbedPane.getComponentAt(2)).getComponent(0));
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membatalkan reservasi!");
            }
        }
    }

    private void updateInfoPanel(JPanel infoPanel) {
        int totalSks = reservasiDAO.getTotalSksReserved(currentNim);

        JLabel totalLabel = (JLabel) ((JPanel) infoPanel).getComponent(0);
        JLabel sisaLabel = (JLabel) ((JPanel) infoPanel).getComponent(2);

        totalLabel.setText("Total SKS: " + totalSks);
        sisaLabel.setText("Sisa SKS: " + (currentMahasiswa.getMaxSks() - totalSks));
        sisaLabel.setForeground(totalSks >= currentMahasiswa.getMaxSks() ? Color.RED : new Color(46, 204, 113));
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

