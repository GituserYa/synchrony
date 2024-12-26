package com.example.synchrony.controller;

import com.example.synchrony.repository.UserRepository;
import com.example.synchrony.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * Controller Class to upload, delete, view the files from Imgur
 **/
@RequestMapping("/api/docs")
@RestController
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    /**
     * Dependency injection for the repository
     * to validate the user and then proceed further
     **/
    @Autowired
    private UserRepository userRepository;

    /**
     * Dependency injection for the image service
     **/
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Post Mapping for saving the file
     * @Param multipart file, Username, and Password
     * and after validating, save the details of the file in the Imgur
     * in the Imgur Software
     **/
    @PostMapping("/upload")
    public ResponseEntity<String> saveDetails(@RequestParam("file") MultipartFile file,
                                              @RequestParam("userName") String userName,
                                              @RequestParam("password") String password) {
        logger.debug("Received request to upload image for user: {}", userName);
        try {
            return imageService.uploadImageToImgur(file, userName, password);
        } catch (IOException e) {
            logger.error("An error occurred while uploading the image: {}", e.getMessage());
            return new ResponseEntity<>("An error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete Mapping for deleting the document that was uploaded
     * on to Imgur Hash code can be obtained from the  database storage
     **/
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDetails(@RequestParam("deleteHashCode") String deleteHashCode,
                                                @RequestParam("userName") String userName,
                                                @RequestParam("password") String password) {
        logger.debug("Received request to delete image with deleteHashCode: {} for user: {}", deleteHashCode, userName);
        try {
            return imageService.deleteImage(deleteHashCode, userName, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**Fetch Code will be the input of the file id that has been saved **/
    @GetMapping("/fetch")
    public ResponseEntity<String> fetchDetails(@RequestParam("fetchCode") String fetchCode) throws IOException {
        logger.debug("Received request to delete image with fetchCode : {]", fetchCode);
        try {
            return imageService.fetchImage(fetchCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sending the file name and the user name for the excahnger of the message broker
     * Currently apache Camel is routed through the producer template and sended out along
     * with the apache kafka broker display
     **/
    @PostMapping("/upload/exchanger")
    public ResponseEntity<String> saveDetailsMessageExchanger(@RequestParam("file") MultipartFile file,
                                              @RequestParam("userName") String userName,
                                              @RequestParam("password") String password) {
        logger.debug("Received request to upload image for user: {}", userName);
        try {
            return imageService.uploadDataToMessengerBroker(file, userName, password);
        } catch (IOException e) {
            logger.error("An error occurred while uploading the image: {}", e.getMessage());
            return new ResponseEntity<>("An error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

