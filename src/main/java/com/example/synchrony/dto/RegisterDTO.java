package com.example.synchrony.dto;

import lombok.Data;

/**DTO class for
 * the input of the registered username
 * and password
 */

public class RegisterDTO {
    private String username;
    private String department;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "RegisterDTO{" +
                "username='" + username + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
