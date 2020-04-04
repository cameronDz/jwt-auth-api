package org.md.api.auth.model.exception;

public class InvalidTokenException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidTokenException() {
        super("Invalid Token");
    }
    
    public InvalidTokenException(String message) {
        super(message);
    }
}
