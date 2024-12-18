import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AuctionAdminPanel extends JFrame {
    private JTextField itemNameField;
    private JTextField priceField;
    private JTextField imageField;
    private JTextField timeField;
    private JTable table;
    private JLabel imageLabel;
    private String imagePath;
    private JButton startAuctionButton;

    public AuctionAdminPanel() {
        setTitle("Admin Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        JLabel adminLabel = new JLabel("ADMIN PANEL");
        adminLabel.setBounds(200, 10, 200, 30);
        adminLabel.setFont(new Font("Serif", Font.BOLD, 24));
        adminLabel.setForeground(Color.BLUE);
        add(adminLabel);



        JLabel itemNameLabel = new JLabel("ITEM NAME:");
        itemNameLabel.setBounds(20, 60, 100, 25);
        itemNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(itemNameLabel);

        itemNameField = new JTextField();
        itemNameField.setBounds(120, 60, 150, 25);
        add(itemNameField);

        JLabel priceLabel = new JLabel("PRICE:");
        priceLabel.setBounds(20, 100, 100, 25);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(120, 100, 150, 25);
        add(priceField);

        JLabel selectImageLabel = new JLabel("SELECT IMAGE:");
        selectImageLabel.setBounds(20, 140, 100, 25);
        selectImageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(selectImageLabel);

        imageField = new JTextField();
        imageField.setBounds(120, 140, 150, 25);
        add(imageField);

        JButton selectImageButton = new JButton("SELECT IMAGE");
        selectImageButton.setBounds(120, 170, 150, 25);
        selectImageButton.setBackground(Color.CYAN);
        selectImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    imageField.setText(selectedFile.getAbsolutePath());
                    imagePath = selectedFile.getAbsolutePath();
                    ImageIcon imageIcon = new ImageIcon(imagePath);
                    imageLabel.setIcon(imageIcon);
                }
            }
        });
        add(selectImageButton);

        imageLabel = new JLabel();
        imageLabel.setBounds(450, 60, 300, 300);
        add(imageLabel);

        JLabel timeLabel = new JLabel("SET TIMER (secs):");
        timeLabel.setBounds(20, 210, 150, 25);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(timeLabel);

        timeField = new JTextField();
        timeField.setBounds(170, 210, 100, 25);
        add(timeField);

        JButton addItemButton = new JButton("ADD ITEM");
        addItemButton.setBounds(20, 250, 100, 25);
        addItemButton.setBackground(Color.GREEN);
        add(addItemButton);

        startAuctionButton = new JButton("START AUCTION");
        startAuctionButton.setBounds(130, 250, 150, 25);
        startAuctionButton.setBackground(Color.ORANGE);
        startAuctionButton.setEnabled(false); // Initially disabled
        add(startAuctionButton);

        JButton backButton = new JButton("BACK");
        backButton.setBounds(290, 250, 100, 25);
        backButton.setBackground(Color.YELLOW);
        backButton.addActionListener(e -> {
            dispose(); // Close current panel
            new SellerBuyerPage(); // Open main Seller-Buyer page
        });
        add(backButton);

        String[] columnNames = {"Item Name", "Image", "Price"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 1) {
                    return ImageIcon.class;
                }
                return Object.class;
            }
        };
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(100);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 290, 760, 300);
        add(scrollPane);

        addItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                String price = priceField.getText();
                if (!itemName.isEmpty() && !price.isEmpty() && imagePath != null) {
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                    model.addRow(new Object[]{itemName, imageIcon, price, "", ""});
                    itemNameField.setText("");
                    priceField.setText("");
                    imageField.setText("");
                    imagePath = null;
                    imageLabel.setIcon(null);
                    startAuctionButton.setEnabled(true); // Enable auction start button
                } else {
                    JOptionPane.showMessageDialog(AuctionAdminPanel.this, "Please enter item name, price, and select an image.");
                }
            }
        });

        startAuctionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String itemName = (String) model.getValueAt(selectedRow, 0);
                    String price = (String) model.getValueAt(selectedRow, 2);
                    int auctionTime = Integer.parseInt(timeField.getText());

                    // Store auction data in AuctionData singleton
                    AuctionData.getInstance().setAuctionData(itemName, Integer.parseInt(price), imagePath, auctionTime);

                    // Show a message confirming the auction start
                    JOptionPane.showMessageDialog(AuctionAdminPanel.this, "Auction started for " + itemName);
                } else {
                    JOptionPane.showMessageDialog(AuctionAdminPanel.this, "Please select an item to start auction.");
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SellerBuyerPage::new);
    }
}






