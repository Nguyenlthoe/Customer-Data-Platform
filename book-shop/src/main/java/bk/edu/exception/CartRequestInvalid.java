package bk.edu.exception;

public class CartRequestInvalid extends RuntimeException{
    public CartRequestInvalid(String message){
        super(message);
    }
}
