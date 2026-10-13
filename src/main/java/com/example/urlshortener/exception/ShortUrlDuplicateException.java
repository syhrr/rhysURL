package com.example.urlshortener.exception;

public class ShortUrlDuplicateException extends RuntimeException {
    private final String code;

    public ShortUrlDuplicateException(String code){
        super("The short url /"+ code+ " already exists");
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}


