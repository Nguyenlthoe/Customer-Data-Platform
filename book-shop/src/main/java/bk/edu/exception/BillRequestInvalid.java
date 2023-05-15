package bk.edu.exception;

public class BillRequestInvalid extends RuntimeException{
    public BillRequestInvalid(String message){
        super(message);
    }
}
