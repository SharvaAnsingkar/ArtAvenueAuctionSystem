import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Registration extends JFrame {
    private JTextField txtName, txtEmail, txtPhone;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnRegister;

    public Registration() {
        setTitle("Art Auction System - Registration");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with padding and layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title Label
        JLabel titleLabel = new JLabel("Register for Art Auction System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));

        // Form fields
        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("Confirm Password:"));
        txtConfirmPassword = new JPasswordField();
        formPanel.add(txtConfirmPassword);

        formPanel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        formPanel.add(txtPhone);

        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Register button panel
        JPanel buttonPanel = new JPanel();
        btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setBackground(new Color(70, 130, 180));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        buttonPanel.add(btnRegister);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set up frame
        add(mainPanel);
        setVisible(true);
    }

    // Method to register user and save details to database
    private void registerUser() {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String phone = txtPhone.getText();

        // Input validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database connection and insert query
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/art_auction", "root", "kobebryant1532");
            String sql = "INSERT INTO users (name, email, password, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, phone);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful! Redirecting to login...", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close registration form
                new Login(); // Open login form
            }
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Registration();
            }
        });
    }
}
