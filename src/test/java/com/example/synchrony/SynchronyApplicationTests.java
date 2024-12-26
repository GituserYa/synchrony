package com.example.synchrony;

import com.example.synchrony.Exception.DBException;
import com.example.synchrony.dto.GetUserResponseDTO;
import com.example.synchrony.dto.RegisterDTO;
import com.example.synchrony.entity.User;
import com.example.synchrony.repository.UserRepository;
import com.example.synchrony.service.UserServiceImpl;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class SynchronyApplicationTests {
    private ServletContext mockServletContext;
    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;
    @InjectMocks
    private UserServiceImpl userService;

    @Spy
    private ModelMapper modelMapper;

    private RegisterDTO registerDTO;
    private User user;
    @BeforeEach
    void setup() {
        // Mock ServletContext
        MockitoAnnotations.openMocks(this);
        mockServletContext = mock(ServletContext.class);

        // Create mock beans
        ModelMapper modelMapper = new ModelMapper();
        RedisConnectionFactory redisConnectionFactory = mock(RedisConnectionFactory.class);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setThreadNamePrefix("Synchrony-Async-");
        taskExecutor.initialize();

        // Simulate bean retrieval in ServletContext
        when(mockServletContext.getAttribute("modelMapper")).thenReturn(modelMapper);
        when(mockServletContext.getAttribute("redisConnectionFactory")).thenReturn(redisConnectionFactory);
        when(mockServletContext.getAttribute("redisTemplate")).thenReturn(redisTemplate);
        when(mockServletContext.getAttribute("asyncTaskExecutor")).thenReturn(taskExecutor);

        registerDTO = new RegisterDTO();
        registerDTO.setUsername("John");
        registerDTO.setDepartment("DOP");

        user = new User();
        user.setId(1L);
        user.setUsername("John");
        user.setDepartment("DOP");

      //  RedisTemplate<String, Object> redisTemplate = mock(RedisTemplate.class);
        ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);

    }

    @Test
    void modelMapperBeanExists() {
        ModelMapper modelMapper = (ModelMapper) mockServletContext.getAttribute("modelMapper");
        assertThat(modelMapper).isNotNull();
    }

    @Test
    void redisConnectionFactoryBeanExists() {
        RedisConnectionFactory redisConnectionFactory = (RedisConnectionFactory) mockServletContext.getAttribute("redisConnectionFactory");
        assertThat(redisConnectionFactory).isNotNull();
    }

    @Test
    void redisTemplateBeanExists() {
        RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>) mockServletContext.getAttribute("redisTemplate");
        assertThat(redisTemplate).isNotNull();
        assertThat(redisTemplate.getConnectionFactory()).isNotNull();
    }

    @Test
    void asyncTaskExecutorBeanExists() {
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) mockServletContext.getAttribute("asyncTaskExecutor");
        assertThat(taskExecutor).isNotNull();
        assertThat(taskExecutor.getCorePoolSize()).isEqualTo(5);
        assertThat(taskExecutor.getMaxPoolSize()).isEqualTo(20);
        assertThat(taskExecutor.getThreadNamePrefix()).isEqualTo("Synchrony-Async-");
    }


}



