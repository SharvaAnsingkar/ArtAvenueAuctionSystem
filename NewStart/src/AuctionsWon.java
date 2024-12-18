import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionListener;

public class AuctionsWon extends JFrame {
    private JTable winnersTable;
    private DefaultTableModel tableModel;

    public AuctionsWon() {
        setTitle("Auctions Won");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set up the table to show auction winners
        tableModel = new DefaultTableModel(new String[]{"Item Name", "Bidder Name", "Bid Price"}, 0);
        winnersTable = new JTable(tableModel);
        add(new JScrollPane(winnersTable), BorderLayout.CENTER);

        // Create a panel for buttons with FlowLayout for alignment
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(buttonPanel, BorderLayout.SOUTH);

        // Back button
        buttonPanel.add(createButton("Back", e -> {
            dispose();
            new SellerBuyerPage();
        }));

        // Load auction winners from the database when the page is created
        loadAuctionWinners();

        setVisible(true);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private void loadAuctionWinners() {
        tableModel.setRowCount(0);  // Clear existing rows before loading fresh data
        SwingUtilities.invokeLater(() -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/art_auction", "root", "kobebryant1532")) {
                String sql = "SELECT item_name, bidder_name, bid_price FROM auction_winners";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{rs.getString("item_name"), rs.getString("bidder_name"), rs.getInt("bid_price")});
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading auction winners.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AuctionsWon::new);
    }
}
