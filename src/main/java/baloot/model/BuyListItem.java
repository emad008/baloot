package baloot.model;

import java.util.HashMap;
import java.util.Map;

public class BuyListItem extends Model {
    private String username;
    private int commodityId;

    public BuyListItem(String username, int commodityId) {
        this.username = username;
        this.commodityId = commodityId;
    }

    public String getUsername() {
        return username;
    }

    public int getCommodityId() {
        return commodityId;
    }

    @Override
    public Map<String, Object> getKey() {
        return Map.of(
            "username",
            this.getUsername(),
            "commodityId",
            this.getCommodityId()
        );
    }

    @Override
    public Map<String, Object> describe() {
        Map<String, Object> res = new HashMap<>();

        res.put("username", this.getUsername());
        res.put("commodityId", this.getCommodityId());
        return res;
    }
}
