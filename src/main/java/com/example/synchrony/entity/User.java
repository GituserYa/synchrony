package com.example.synchrony.entity;

import jakarta.persistence.*;
import lombok.Data;


/**@Entity for storing the
 * records of the users
 **/
@Entity
@Data
@Table(name = "registration_records")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username_record")
    private String username;
    @Column(name = "password_record")
    private String password;

}

