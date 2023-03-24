package baloot.exception;

public class BalootException extends RuntimeException {
    BalootException() {
        super();
    }

    BalootException(String message) {
        super(message);
    }
}
