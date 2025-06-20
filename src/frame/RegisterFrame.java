package frame;

import dao.MahasiswaDAO;
import db_connection.Mahasiswa;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField nimField, namaField, emailField;
    private JPasswordField passwordField;
    private JSpinner semesterSpinner, ipkSpinner;
    private MahasiswaDAO mahasiswaDAO;

    public RegisterFrame() {
        mahasiswaDAO = new MahasiswaDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Registrasi - Sistem Reservasi SKS");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 20, 20));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Registrasi Mahasiswa Baru"),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        formPanel.add(new JLabel("NIM:"));
        nimField = new JTextField();
        nimField.setPreferredSize(new Dimension(300, 25));
        formPanel.add(nimField);

        formPanel.add(new JLabel("Nama:"));
        namaField = new JTextField();
        formPanel.add(namaField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Semester:"));
        semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 14, 1));
        formPanel.add(semesterSpinner);

        formPanel.add(new JLabel("IPK:"));
        ipkSpinner = new JSpinner(new SpinnerNumberModel(2.0, 0.0, 4.0, 0.01));
        formPanel.add(ipkSpinner);

        JButton registerButton = new JButton("Daftar");
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.BLACK);
        registerButton.addActionListener(e -> performRegister());
        formPanel.add(registerButton);

        JButton cancelButton = new JButton("Batal");
        cancelButton.addActionListener(e -> dispose());
        formPanel.add(cancelButton);

        mainPanel.add(formPanel, gbc);
        add(mainPanel, BorderLayout.CENTER);

        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void performRegister() {
        String nim = nimField.getText().trim();
        String nama = namaField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        int semester = (Integer) semesterSpinner.getValue();
        double ipk = (Double) ipkSpinner.getValue();

        if (nim.isEmpty() || nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }

        Mahasiswa mahasiswa = new Mahasiswa(nim, nama, email, password, semester, ipk);

        if (mahasiswaDAO.register(mahasiswa)) {
            JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registrasi gagal! NIM mungkin sudah terdaftar.");
        }
    }
}