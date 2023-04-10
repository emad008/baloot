package com.baloot.exception;

public class NoCommodityAvailable extends BalootException {
    private final String commodityName;

    public NoCommodityAvailable(String commodityName) {
        this.commodityName = commodityName;
    }

    @Override
    public String getMessage() {
        return "Not enough stock for commodity " + commodityName;
    }
}
