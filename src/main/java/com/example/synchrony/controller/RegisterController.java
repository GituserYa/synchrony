package com.example.synchrony.controller;

import com.example.synchrony.Exception.CustomException;
import com.example.synchrony.Exception.DBException;
import com.example.synchrony.dto.GetUserResponseDTO;
import com.example.synchrony.dto.RegisterDTO;
import com.example.synchrony.dto.ResponseDTO;
import com.example.synchrony.entity.User;
import com.example.synchrony.service.UserService;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/register")
@RestController
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    /**
     * Dependency injection for the user service
     **/
    @Autowired
    private UserService userService;


    /**
     * Post Mapping for saving the @username and @password
     *
     * @param RegisterDTO
     **/
    @PostMapping("/user")
    public ResponseEntity<ResponseDTO> saveDetails(@RequestBody RegisterDTO registerDTO) throws CustomException {
        logger.info("Received request to register user: {}", registerDTO);
        userService.saveData(registerDTO);
        return new ResponseEntity<>(new ResponseDTO("DATA SAVED SUCCESSFULLY"), HttpStatus.OK);
    }

    @GetMapping("/userDetails/{id}")
    public ResponseEntity<GetUserResponseDTO> getUserDetails(@PathVariable Long id) throws DBException {
        logger.info("Received request to find user with id : {}", id);
       var response= userService.getRecordById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/userDetails/{id}")
    public ResponseEntity<ResponseDTO> deleteUserDetails(@PathVariable Long id) throws DBException {
        logger.info("Received request to find user with id : {}", id);
      userService.deleteRecord(id);
        return new ResponseEntity<>(new ResponseDTO("DATA DELETED SUCCESFFULLY"), HttpStatus.OK);
    }
}
