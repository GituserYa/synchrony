package com.example.synchrony.dto;

import lombok.Data;

/**DTO class for
 * the input of the registered username
 * and password
 */
@Data
public class RegisterDTO {
    private String username;
    private String password;
}
