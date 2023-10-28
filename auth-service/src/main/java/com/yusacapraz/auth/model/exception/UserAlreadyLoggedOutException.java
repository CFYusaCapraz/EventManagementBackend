package com.yusacapraz.auth.model.exception;

public class UserAlreadyLoggedOutException extends RuntimeException {
    public UserAlreadyLoggedOutException(String message) {
        super(message);
    }
}
