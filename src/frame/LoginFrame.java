package frame;
import dao.*;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField nimField;
    private JPasswordField passwordField;
    private MahasiswaDAO mahasiswaDAO;

    public LoginFrame() {
        mahasiswaDAO = new MahasiswaDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - Sistem Reservasi SKS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        JLabel titleLabel = new JLabel("SISTEM RESERVASI SKS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        // Login Form
        JPanel loginPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        loginPanel.setBorder(BorderFactory.createTitledBorder("Login Mahasiswa"));
        loginPanel.setPreferredSize(new Dimension(300, 250));

        loginPanel.add(new JLabel("NIM:"));
        nimField = new JTextField();
        loginPanel.add(nimField);

        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.BLACK);
        loginButton.addActionListener(e -> performLogin());
        loginPanel.add(loginButton);

        JButton registerButton = new JButton("Registrasi");
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.BLACK);
        registerButton.addActionListener(e -> openRegister());
        loginPanel.add(registerButton);

        mainPanel.add(loginPanel, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void performLogin() {
        String nim = nimField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (nim.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM dan Password harus diisi!");
            return;
        }

        if (nim.equals("1") && password.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Login sebagai Admin berhasil!");
            this.dispose();
            new AdminFrame().setVisible(true);
        } else if (mahasiswaDAO.login(nim, password)) {
            JOptionPane.showMessageDialog(this, "Login berhasil!");
            this.dispose();
            new MainFrame(nim).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "NIM atau Password salah!");
        }
    }

    private void openRegister() {
        new RegisterFrame().setVisible(true);
    }
}