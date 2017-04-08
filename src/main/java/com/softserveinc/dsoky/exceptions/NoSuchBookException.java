package com.softserveinc.dsoky.exceptions;

public class NoSuchBookException extends RuntimeException{
    public NoSuchBookException(String message) {
        super(message);
    }
}
