package com.softserveinc.dsoky.exceptions;

public class NoSuchAuthorException extends RuntimeException{
    public NoSuchAuthorException(String message){
        super(message);
    }
}
