package com.example.synchrony.Exception;

import org.springframework.http.HttpStatus;

public class DBException extends CustomException{

    public DBException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
