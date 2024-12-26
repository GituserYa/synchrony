package com.example.synchrony.repository;

import com.example.synchrony.entity.Image;
import com.example.synchrony.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**@Repository  to fetch the data from the databse if required**/
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    /**Custom query to find the record from the Database with the image hashcode that was
     * generated in the Imagur Software**/
    @Query(value = "SELECT * FROM image_records WHERE hash_code = :hashcode", nativeQuery = true)
    Image findRecordByHashCode(String hashcode);


}
