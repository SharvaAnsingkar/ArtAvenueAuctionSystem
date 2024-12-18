import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public Login() {
        setTitle("Login Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Setting up components
        JLabel l1 = new JLabel("Username:");
        JLabel l2 = new JLabel("Password:");
        txtUsername = new JTextField();
        txtPassword = new JPasswordField(); // Using JPasswordField for password input
        btnLogin = new JButton("Login");

        l1.setBounds(50, 100, 100, 30);
        l2.setBounds(50, 150, 100, 30);
        txtUsername.setBounds(150, 100, 200, 30);
        txtPassword.setBounds(150, 150, 200, 30);
        btnLogin.setBounds(150, 200, 100, 30);

        add(l1);
        add(l2);
        add(txtUsername);
        add(txtPassword);
        add(btnLogin);

        setLayout(null);
        setVisible(true);

        // Action listener for login button
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        // Check if inputs are not empty
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database connection and user validation
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/art_auction", "root", "kobebryant1532");
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close login form
                new SellerBuyerPage(); // Open Seller-Buyer page
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
