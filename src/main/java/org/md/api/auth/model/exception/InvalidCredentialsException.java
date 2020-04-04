package org.md.api.auth.model.exception;

public class InvalidCredentialsException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException() {
        super("Invalid Credentials");
    }
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
