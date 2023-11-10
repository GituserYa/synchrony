package com.example.synchrony.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.synchrony.entity.Image;
import com.example.synchrony.entity.User;
import com.example.synchrony.repository.ImageRepository;
import com.example.synchrony.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;
    private CamelContext camelContext;



 /**Service Implementation Method to upload the image to the imagur site with
  * @param Multipart file
  * @Param  userName, password**/
    @Override
    public ResponseEntity<String> uploadImageToImgur(MultipartFile file, String userName, String password) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        try {
            Optional<User> details = userRepository.findUserByNameAndPassword(userName, password);
            /**Calling the rest template and setting the header along with the accont credentials
             * at imgur
             */
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Client-ID ed30199f368e24d");

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new HttpEntity<>(file.getResource(), headers));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "https://api.imgur.com/3/image", HttpMethod.POST, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());

            /**Setting the relation in the database to link the images asscoaited with the
             * database after the standalon images from the imgur software has been deleted
             */
            // Access the deletehash and link values from the JSON response
            String deleteHash = rootNode.at("/data/deletehash").asText();
            String imageId= rootNode.at("/data/id").asText();
            String link = rootNode.at("/data/link").asText();

           /** Setting the image and setting the relation with the database for the association**/
            Image image = new Image();
            image.setHashCode(deleteHash);
            image.setImageLink(link);
            image.setImageId(imageId);
            image.setUser(details.get());
            imageRepository.save(image);

            logger.info("Image uploaded successfully for user: {}", userName);

            return responseEntity;
        } catch (Exception e) {
            logger.error("Error uploading image for user: {}", userName, e);
            throw e;
        }
    }


    /**Service Implementation Method to delete  the image from  the imagur site with
     * @param delete hash code
     * @Param  userName, password(Only for the database deletion as the record are mainatained nin the databse for the proper association with the user)**/
    @Override
    public ResponseEntity<String> deleteImage(String deleteHashCode, String userName, String password) {

        /**Calling the rest template and setting the header along with the accont credentials
         * at imgur
         */
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Client-ID ed30199f368e24d");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);

        String imgurDeleteUrl = "https://api.imgur.com/3/image/" + deleteHashCode;
        var response = restTemplate.exchange(
                imgurDeleteUrl, HttpMethod.DELETE, requestEntity, String.class);
        /**Once the image gets deleted from the imgur the association that is
         * established in the database to keep a track of the
         * username and password has to be deleted as well
         */
        Image imageRecord = imageRepository.findRecordByHashCode(deleteHashCode);
        imageRepository.delete(imageRecord);

        logger.info("Image deleted successfully for user: {}", userName);

        return response;
    }


    /**Service Implementation Method to get  the image from  the imagur site with
     * @param fetchCode
     * @Param  userName, password(Only for the database deletion as the record are maintained in the database for the proper association with the user)**/
    @Override
    public ResponseEntity<String> fetchImage(String fetchCode) {
        /**Calling the rest template and setting the header along with the account credentials
         * at imgur
         */
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Client-ID ed30199f368e24d");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);

        String imgurFetchUrl = "https://api.imgur.com/3/image/" + fetchCode;
        var response = restTemplate.exchange(
                imgurFetchUrl, HttpMethod.GET, requestEntity, String.class);

        logger.info("Image fetched successfully for user: {}", fetchCode);

        return response;
    }

    @Override
    public ResponseEntity<String> uploadDataToMessengerBroker(MultipartFile file, String userName, String password) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        try {
            Optional<User> details = userRepository.findUserByNameAndPassword(userName, password);
            /**Calling the rest template and setting the header along with the accont credentials
             * at imgur
             */
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Client-ID ed30199f368e24d");

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new HttpEntity<>(file.getResource(), headers));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    "https://api.imgur.com/3/image", HttpMethod.POST, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());

            /**Setting the relation in the database to link the images asscoaited with the
             * database after the standalon images from the imgur software has been deleted
             */
            // Access the deletehash and link values from the JSON response
            String deleteHash = rootNode.at("/data/deletehash").asText();
            String imageId= rootNode.at("/data/id").asText();
            String link = rootNode.at("/data/link").asText();

            /** Setting the image and setting the relation with the database for the association**/
            Image image = new Image();
            image.setHashCode(deleteHash);
            image.setImageLink(link);
            image.setImageId(imageId);
            image.setUser(details.get());
            imageRepository.save(image);

            /**Creating the producer template to direct the user name associate with the image to the
             * to the route which can then publish it to the kafka  **/

            String message = "username" + userName + "Imageid"+imageId;
            camelContext.createProducerTemplate().sendBody("direct:publishToMessagingPlatform", message);
            logger.info("Image uploaded successfully for user: {}", userName);

            return responseEntity;
        } catch (Exception e) {
            logger.error("Error uploading image for user: {}", userName, e);
            throw e;
        }
    }
}
