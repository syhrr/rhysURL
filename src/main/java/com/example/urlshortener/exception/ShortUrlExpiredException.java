package com.example.urlshortener.exception;

public class ShortUrlExpiredException extends RuntimeException {
    private final String code;

    public ShortUrlExpiredException(String code){
        super("The code: "+ code +" has expired!");
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}


