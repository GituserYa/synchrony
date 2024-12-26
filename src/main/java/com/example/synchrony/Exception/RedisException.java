package com.example.synchrony.Exception;

import org.springframework.http.HttpStatus;

public class RedisException extends CustomException{
    public RedisException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
