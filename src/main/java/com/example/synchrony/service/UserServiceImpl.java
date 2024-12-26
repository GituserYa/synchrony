package com.example.synchrony.service;

import com.example.synchrony.Exception.DBException;
import com.example.synchrony.Exception.RedisException;
import com.example.synchrony.dto.GetUserResponseDTO;
import com.example.synchrony.dto.RegisterDTO;
import com.example.synchrony.entity.User;
import com.example.synchrony.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service Implementation class
 * for saving the registered user
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private final AtomicInteger cacheHitCount = new AtomicInteger(0);
    private final AtomicInteger cacheMissCount = new AtomicInteger(0);

    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    /**
     * Implementation to save the user details in the service repository
     **/
    @Async("asyncTaskExecutor")
    @Override
    public void saveData(RegisterDTO registerDTO) throws DBException {
        try {
            long startTime = System.currentTimeMillis();
            User savedRecord = userRepository.save(this.mappingToEntity(registerDTO));
            try {
                redisTemplate.opsForValue().set("record:" + savedRecord.getId(), savedRecord);
            } catch (Exception ex) {
                throw new RedisException("Error occurred while interacting with Redis: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            long endTime = System.currentTimeMillis();
            logger.info("Save operation completed in {} ms", (endTime - startTime));
        } catch (Exception ex) {
            throw new DBException("Error occurred while saving" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public GetUserResponseDTO getRecordById(Long id) throws DBException {
        try {
            String cacheKey = "record:" + id;

            // Check cache for the record
            User cachedRecord = (User) redisTemplate.opsForValue().get(cacheKey);
            if (cachedRecord != null) {
                cacheHitCount.incrementAndGet();
                // Return response from cache
                return buildResponseFromUser(cachedRecord);
            }

            // Cache miss
            cacheMissCount.incrementAndGet();
            Optional<User> dbRecord = userRepository.findById(id);

            if (dbRecord.isPresent()) {
                // Save to cache
                try {
                    redisTemplate.opsForValue().set(cacheKey, dbRecord.get());
                    // Return response from DB
                } catch (Exception ex) {
                    throw new RedisException("Error occurred while interacting with Redis: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return buildResponseFromUser(dbRecord.get());
            }
            // Handle case where record is not found in DB
            throw new NoSuchElementException("User not found with ID: " + id);
        } catch (Exception ex) {
            logger.error("Error occured while fetching the Data for the user");
            throw new DBException("Error occurred while fetching Data " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private GetUserResponseDTO buildResponseFromUser(User user) {
        GetUserResponseDTO response = new GetUserResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setDepartment(user.getDepartment());
        return response;
    }


    /**
     * Implementation to delete user details in the  repository
     * and deleting the record from the redis
     * so that not retrieved
     **/
    @Async("asyncTaskExecutor")
    @Transactional
    public void deleteRecord(Long id) throws DBException {
        try {
            userRepository.deleteById(id);
            try {
                redisTemplate.delete("record:" + id);
            } catch (Exception ex) {
                throw new RedisException("Error occurred while interacting with Redis: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            logger.error("Error occurred while deleting the record ");
            throw new DBException("Error occurred while deleting" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Mapping DTO to the Entity
     **/
    private User mappingToEntity(RegisterDTO registerDTO) {
        return modelMapper.map(registerDTO, User.class);
    }

}
