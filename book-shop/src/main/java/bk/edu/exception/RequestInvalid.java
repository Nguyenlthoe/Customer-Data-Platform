package bk.edu.exception;

public class RequestInvalid extends RuntimeException{
    public RequestInvalid(String message) {
        super(message);
    }
}
