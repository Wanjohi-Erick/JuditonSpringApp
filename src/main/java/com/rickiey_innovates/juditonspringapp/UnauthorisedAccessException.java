package com.rickiey_innovates.juditonspringapp;

public class UnauthorisedAccessException extends RuntimeException{
    public UnauthorisedAccessException (String message) {
        super(message);
    }

    private UnauthorisedAccessException (String message, Throwable cause) {
        super(message, cause);
    }
}
