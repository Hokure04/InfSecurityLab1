package com.example.infsecuritylab1.exception;

public class AuthorizeException extends RuntimeException{
    public AuthorizeException(String problemText){
        super(problemText);
    }
}
