package com.example.synchrony.controller;

import com.example.synchrony.dto.RegisterDTO;
import com.example.synchrony.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/register")
@RestController
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    /**
     * Dependency injection for the user service
     **/
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Post Mapping for saving the @username and @password
     * @param RegisterDTO in the Imgur Software
     *  First save the username and password
     **/
    @PostMapping("/user")
    public String saveDetails(@RequestBody RegisterDTO registerDTO) {
        logger.info("Received request to register user: {}", registerDTO.getUsername());
        try {
            return userService.save(registerDTO);
        } catch (Exception e) {
            logger.error("An error occurred while processing the request: {}", e.getMessage());
            return "An error occurred. Please try again later.";
        }
    }
}
