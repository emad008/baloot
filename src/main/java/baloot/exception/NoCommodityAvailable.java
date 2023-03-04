package baloot.exception;

import baloot.model.Commodity;

public class NoCommodityAvailable extends RuntimeException {
    private final String commodityName;

    public NoCommodityAvailable(String commodityName) {
        this.commodityName = commodityName;
    }

    @Override
    public String getMessage() {
        return "Not enough stock for commodity " + commodityName;
    }
}
