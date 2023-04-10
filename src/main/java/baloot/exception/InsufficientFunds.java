package baloot.exception;


public class InsufficientFunds extends BalootException {
    private final String username;
    private final int wantedAmount;
    private final int currentAmount;


    public InsufficientFunds(
        String username,
        int wantedAmount,
        int currentAmount
    ) {
        this.username = username;
        this.wantedAmount = wantedAmount;
        this.currentAmount = currentAmount;
    }
}
