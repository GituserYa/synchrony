package com.example.synchrony.service;

import com.example.synchrony.dto.RegisterDTO;
import com.example.synchrony.entity.User;
import com.example.synchrony.repository.UserRepository;
import lombok.extern.slf4j.Slf4j; // Import the Lombok logger annotation
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/** Service Implementation class
 * for saving the registered user
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    /**Implementation to save the user details in the service repository**/
    @Override
    public String save(RegisterDTO registerDTO) {
        userRepository.save(this.mappingToEntity(registerDTO));
        log.info("User saved successfully: {}", registerDTO.getUsername()); // Log the user's username
        return "Data saved Successfully";
    }

    /**Mapping DTO to the Entity **/
    private User mappingToEntity(RegisterDTO registerDTO) {
        return modelMapper.map(registerDTO, User.class);
    }
    /**Mapping Entity to the user **/
    private RegisterDTO mappingToDTO(User user) {
        return modelMapper.map(user, RegisterDTO.class);
    }
}
