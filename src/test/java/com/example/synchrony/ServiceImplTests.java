package com.example.synchrony;

import com.example.synchrony.Exception.DBException;
import com.example.synchrony.dto.GetUserResponseDTO;
import com.example.synchrony.dto.RegisterDTO;
import com.example.synchrony.entity.User;
import com.example.synchrony.repository.UserRepository;
import com.example.synchrony.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
@SpringBootTest
public class ServiceImplTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterDTO registerDTO;
    private User user;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock RedisTemplate behavior
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Prepare test data
        registerDTO = new RegisterDTO();
        registerDTO.setUsername("John");
        registerDTO.setDepartment("Engineering");

        user = new User();
        user.setId(1L);
        user.setUsername("John");
        user.setDepartment("Engineering");


    }

    @Test
    void testSave_Success() throws DBException {
        when(modelMapper.map(registerDTO, User.class)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        doNothing().when(valueOperations).set(eq("record:1"), any(User.class));

        userService.saveData(registerDTO);

        verify(userRepository, times(1)).save(any(User.class));
        verify(valueOperations, times(1)).set(eq("record:1"), any(User.class));
    }

    @Test
    void testSave_ThrowsDBException() {
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        DBException exception = assertThrows(DBException.class, () -> userService.saveData(registerDTO));

        assertEquals("Error occurred while savingError occurred while interacting with Redis: Cannot invoke \"com.example.synchrony.entity.User.getId()\" because \"savedRecord\" is null", exception.getMessage());
    }

    @Test
    void testSave_ThrowsRedisException() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        doThrow(new RuntimeException("Redis error")).when(valueOperations).set(eq("record:1"), any(User.class));

        DBException exception = assertThrows(DBException.class, () -> userService.saveData(registerDTO));

        assertEquals("Error occurred while savingError occurred while interacting with Redis: Cannot invoke \"com.example.synchrony.entity.User.getId()\" because \"savedRecord\" is null", exception.getMessage());
    }

    @Test
    void testGetRecordById_CacheHit() throws DBException {
        when(valueOperations.get("record:1")).thenReturn(user);

        GetUserResponseDTO response = userService.getRecordById(1L);

        assertNotNull(response);
        assertEquals("John", response.getUsername());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void testGetRecordById_CacheMissAndDatabaseHit() throws DBException {
        when(valueOperations.get("record:1")).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        GetUserResponseDTO response = userService.getRecordById(1L);

        assertNotNull(response);
        assertEquals("John", response.getUsername());
        verify(valueOperations, times(1)).set(eq("record:1"), any(User.class));
    }

    @Test
    void testGetRecordById_NotFound() {
        when(valueOperations.get("record:1")).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        DBException exception = assertThrows(DBException.class, () -> userService.getRecordById(1L));

        assertEquals("Error occurred while fetching Data User not found with ID: 1", exception.getMessage());
    }


    @Test
    void testDeleteRecord_ThrowsDBException() {
        doThrow(new RuntimeException("Database error")).when(userRepository).deleteById(1L);

        DBException exception = assertThrows(DBException.class, () -> userService.deleteRecord(1L));

        assertEquals("Error occurred while deletingDatabase error", exception.getMessage());
    }

    @Test
    void testDeleteRecord_ThrowsRedisException() {
        doNothing().when(userRepository).deleteById(1L);
        doThrow(new RuntimeException("Redis error")).when(redisTemplate).delete("record:1");

        DBException exception = assertThrows(DBException.class, () -> userService.deleteRecord(1L));

        assertEquals("Error occurred while deletingError occurred while interacting with Redis: Redis error", exception.getMessage());
    }



    @Test
    void testSaveThrowsDBException() {
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        DBException exception = assertThrows(DBException.class, () -> userService.saveData(registerDTO));

        assertEquals("Error occurred while savingError occurred while interacting with Redis: Cannot invoke \"com.example.synchrony.entity.User.getId()\" because \"savedRecord\" is null", exception.getMessage());
    }

    @Test
    void testDeleteRecordThrowsDBException() {
        doThrow(new RuntimeException("Database error")).when(userRepository).deleteById(1L);

        DBException exception = assertThrows(DBException.class, () -> userService.deleteRecord(1L));

        assertEquals("Error occurred while deletingDatabase error", exception.getMessage());
    }
}
