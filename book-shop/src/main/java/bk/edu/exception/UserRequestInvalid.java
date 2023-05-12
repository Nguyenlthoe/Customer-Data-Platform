package bk.edu.exception;

public class UserRequestInvalid extends RuntimeException{
    public UserRequestInvalid(String message) {
        super(message);
    }
}
