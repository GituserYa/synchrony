package com.example.synchrony.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class SaveResponseDTO {
    private String message;
    private String HttpStatus;

    public SaveResponseDTO(String imageUploadedSuccessfully, org.springframework.http.HttpStatus httpStatus) {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHttpStatus() {
        return HttpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        HttpStatus = httpStatus;
    }


}
