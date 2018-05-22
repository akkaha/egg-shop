package cc.akkaha.common.exceptions;

public class DatabaseOperationException extends RuntimeException {

    public DatabaseOperationException(String message) {
        super(message);
    }
}
