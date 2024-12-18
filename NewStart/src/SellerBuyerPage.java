import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SellerBuyerPage extends JFrame {
    private JButton sellerButton;
    private JButton buyerButton;
    private JButton auctionsWonButton;

    public SellerBuyerPage() {
        setTitle("Seller-Buyer Page");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for buttons with FlowLayout for alignment
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        add(buttonPanel, BorderLayout.CENTER);

        // Seller button
        sellerButton = new JButton("SELLER");
        sellerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AuctionAdminPanel();
            }
        });
        buttonPanel.add(sellerButton);

        // Buyer button
        buyerButton = new JButton("BUYER");
        buyerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AuctionCustomerPanel();
            }
        });
        buttonPanel.add(buyerButton);

        // Auctions Won button
        auctionsWonButton = new JButton("AUCTIONS WON");
        auctionsWonButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AuctionsWon();
            }
        });
        buttonPanel.add(auctionsWonButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new SellerBuyerPage();
    }
}
