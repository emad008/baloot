package baloot.model;

import java.util.HashMap;
import java.util.Map;

public class BuyListItem extends Model {
    private String username;
    private int commodityId;
    private Boolean isPurchased;

    public BuyListItem(String username, int commodityId, Boolean isPurchased) {
        this.username = username;
        this.commodityId = commodityId;
        this.isPurchased = isPurchased;
    }

    public BuyListItem(String username, int commodityId) {
        this(username, commodityId, false);
    }

    public String getUsername() {
        return username;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public Boolean getPurchased() {
        return isPurchased;
    }

    public void setPurchased(Boolean purchased) {
        this.isPurchased = purchased;
    }

    @Override
    public Map<String, Object> getKey() {
        return Map.of(
            "username",
            this.getUsername(),
            "commodityId",
            this.getCommodityId(),
            "isPurchased",
            this.getPurchased()
        );
    }

    @Override
    public Map<String, Object> describe() {
        return Map.of(
            "username", this.getUsername(),
            "commodityId", this.getCommodityId(),
            "isPurchased", this.getPurchased()
        );
    }
}
