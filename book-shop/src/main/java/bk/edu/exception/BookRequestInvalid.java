package bk.edu.exception;

public class BookRequestInvalid extends RuntimeException {
    public BookRequestInvalid(String message) {
        super(message);
    }
}
