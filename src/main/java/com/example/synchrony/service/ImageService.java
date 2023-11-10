package com.example.synchrony.service;

import org.apache.camel.CamelContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;



public interface ImageService {
     ResponseEntity<String> uploadImageToImgur(MultipartFile file, String userName, String password ) throws IOException ;

    ResponseEntity<String> deleteImage(String deleteHashCode, String userName, String password);

    ResponseEntity<String> fetchImage(String fetchCode);

    ResponseEntity<String> uploadDataToMessengerBroker(MultipartFile file, String userName, String password) throws IOException;
}
