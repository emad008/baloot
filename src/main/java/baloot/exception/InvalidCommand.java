package baloot.exception;

public class InvalidCommand extends RuntimeException {
    public InvalidCommand(String message) {
        super(message);
    }
}
