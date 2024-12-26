package com.example.synchrony.dto;

public class GetUserResponseDTO {
    private long id;
    private String username;
    private String department;

    @Override
    public String toString() {
        return "GetUserResponseDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", department='" + department + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
