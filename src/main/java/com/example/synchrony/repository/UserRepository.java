package com.example.synchrony.repository;

import com.example.synchrony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**Custom Query to Find the user by the username and password**/
    @Query(value = "SELECT * FROM registration_records WHERE username_record = :username AND password_record = :password", nativeQuery = true)
    Optional<User> findUserByNameAndPassword(String username,String password);

}
