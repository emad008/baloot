package baloot.exception;

public class AlreadyInBuyList extends RuntimeException {
    private String username;
    private String commodityName;

    public AlreadyInBuyList(String username, String commodityName) {
        this.username = username;
        this.commodityName = commodityName;
    }

    @Override
    public String getMessage() {
        return "user " + this.username + " has " + this.commodityName + " in his/her buy list";
    }
}
