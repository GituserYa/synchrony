package com.example.synchrony.entity;

import jakarta.persistence.*;
import lombok.Data;

/**@Entity class Image for saving the details
 * of the Images in the
 * Database*/
@Entity
@Data
@Table(name = "image_records")
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "hash_code")
    private String hashCode;
    @Column(name = "image_url")
    private String imageLink;
    @Column(name="image_id")
    private String imageId;
    @ManyToOne
    @JoinColumn(name = "user_id")
     private User user; ;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }


}
