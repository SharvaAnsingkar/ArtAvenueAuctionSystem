import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.sql.*;

public class AuctionCustomerPanel extends JFrame {
    private JTextField bidderNameField;
    private JTextField bidPriceField;
    private JLabel timerLabel;
    private JLabel itemNameLabel;
    private JLabel priceLabel;
    private JLabel imageLabel;
    private Timer timer;
    private int timeRemaining;
    private JTable bidderTable;
    private DefaultTableModel tableModel;
    private String highestBidder = "";
    private int highestBid = 0;

    public AuctionCustomerPanel() {
        String itemName = AuctionData.getInstance().getItemName();
        int price = AuctionData.getInstance().getPrice();
        String imagePath = AuctionData.getInstance().getImagePath();
        timeRemaining = AuctionData.getInstance().getTimeRemaining();

        setTitle("Customer Panel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel auctionLabel = new JLabel("AUCTION PAGE");
        auctionLabel.setBounds(300, 10, 200, 30);
        auctionLabel.setFont(new Font("Serif", Font.BOLD, 24));
        auctionLabel.setForeground(Color.RED);
        add(auctionLabel);

        timerLabel = new JLabel("TIMER: " + timeRemaining);
        timerLabel.setBounds(500, 10, 200, 30);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 18));
        add(timerLabel);

        this.timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (timeRemaining > 0) {
                    timeRemaining--;
                    timerLabel.setText("TIMER: " + timeRemaining);
                } else {
                    ((Timer) e.getSource()).stop();
                    timerLabel.setText("TIME'S UP!");
                    declareWinner();
                }
            }
        });
        timer.start();

        JLabel itemNameTextLabel = new JLabel("ITEM NAME:");
        itemNameTextLabel.setBounds(50, 60, 100, 30);
        add(itemNameTextLabel);

        itemNameLabel = new JLabel(itemName);
        itemNameLabel.setBounds(160, 60, 300, 30);
        add(itemNameLabel);

        JLabel priceTextLabel = new JLabel("PRICE:");
        priceTextLabel.setBounds(50, 100, 100, 30);
        add(priceTextLabel);

        priceLabel = new JLabel(String.valueOf(price));
        priceLabel.setBounds(160, 100, 300, 30);
        add(priceLabel);

        JLabel imageTextLabel = new JLabel("IMAGE:");
        imageTextLabel.setBounds(50, 140, 100, 30);
        add(imageTextLabel);

        // Image loading and display with checks for existence and scaling
        imageLabel = new JLabel();
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                ImageIcon itemImageIcon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                imageLabel.setIcon(itemImageIcon);
            } else {
                imageLabel.setText("Image file not found at specified path");
            }
        } else {
            imageLabel.setText("No image available");
        }
        imageLabel.setBounds(160, 140, 200, 200);
        add(imageLabel);

        JLabel bidderNameLabel = new JLabel("BIDDER NAME:");
        bidderNameLabel.setBounds(50, 360, 120, 30);
        add(bidderNameLabel);

        bidderNameField = new JTextField();
        bidderNameField.setBounds(160, 360, 200, 30);
        add(bidderNameField);

        JLabel bidPriceLabel = new JLabel("BID PRICE:");
        bidPriceLabel.setBounds(50, 400, 100, 30);
        add(bidPriceLabel);

        bidPriceField = new JTextField();
        bidPriceField.setBounds(160, 400, 200, 30);
        add(bidPriceField);

        String[] columnNames = {"Bidder Name", "Bid Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bidderTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bidderTable);
        scrollPane.setBounds(400, 60, 350, 300);
        add(scrollPane);

        JButton placeBidButton = new JButton("PLACE BID");
        placeBidButton.setBounds(50, 440, 120, 30);
        placeBidButton.setBackground(Color.GREEN);
        placeBidButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bidderName = bidderNameField.getText();
                String bidPrice = bidPriceField.getText();

                if (!bidderName.isEmpty() && !bidPrice.isEmpty()) {
                    try {
                        int bidAmount = Integer.parseInt(bidPrice);
                        int currentPrice = Integer.parseInt(priceLabel.getText());
                        if (bidAmount > currentPrice) {
                            JOptionPane.showMessageDialog(AuctionCustomerPanel.this, "Bid placed successfully!");
                            priceLabel.setText(String.valueOf(bidAmount));
                            bidderNameField.setText("");
                            bidPriceField.setText("");

                            tableModel.addRow(new Object[]{bidderName, bidAmount});

                            if (bidAmount > highestBid) {
                                highestBid = bidAmount;
                                highestBidder = bidderName;
                            }

                        } else {
                            JOptionPane.showMessageDialog(AuctionCustomerPanel.this, "Your bid must be higher than the current price.");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(AuctionCustomerPanel.this, "Please enter a valid bid amount.");
                    }
                } else {
                    JOptionPane.showMessageDialog(AuctionCustomerPanel.this, "Please enter both your name and bid price.");
                }
            }
        });
        add(placeBidButton);

        JButton backButton = new JButton("BACK");
        backButton.setBounds(200, 440, 100, 30);
        backButton.setBackground(Color.YELLOW);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SellerBuyerPage();
            }
        });
        add(backButton);

        setVisible(true);
    }

    private void declareWinner() {
        if (!highestBidder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Auction ended! " + highestBidder + " wins the auction for "
                    + AuctionData.getInstance().getItemName() + " with a bid of " + highestBid);
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/art_auction", "root", "kobebryant1532")) {
                // Only save the winning bid into the database
                String sql = "INSERT INTO auction_winners (item_name, bidder_name, bid_price) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, AuctionData.getInstance().getItemName());
                pstmt.setString(2, highestBidder);
                pstmt.setInt(3, highestBid);
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving auction result.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Auction ended! No bids placed.");
        }

        // After auction ends, navigate to AuctionsWonPage
        dispose();
        new AuctionsWon();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AuctionCustomerPanel::new);
    }
}
