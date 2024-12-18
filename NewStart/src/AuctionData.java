import java.util.ArrayList;

public class AuctionData {
    private static AuctionData instance = null;

    private String itemName;
    private int price;
    private String imagePath;
    private int timeRemaining;
    private ArrayList<String> bidderNames;
    private ArrayList<Integer> bids;

    private AuctionData() {
        bidderNames = new ArrayList<>();
        bids = new ArrayList<>();
    }

    public static AuctionData getInstance() {
        if (instance == null) {
            instance = new AuctionData();
        }
        return instance;
    }

    public void setAuctionData(String itemName, int price, String imagePath, int timeRemaining) {
        this.itemName = itemName;
        this.price = price;
        this.imagePath = imagePath;
        this.timeRemaining = timeRemaining;
    }

    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }


}

