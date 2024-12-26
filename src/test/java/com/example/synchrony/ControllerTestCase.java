package com.example.synchrony;

import com.example.synchrony.controller.RegisterController;
import com.example.synchrony.dto.GetUserResponseDTO;
import com.example.synchrony.dto.RegisterDTO;
import com.example.synchrony.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
public class ControllerTestCase {


        private MockMvc mockMvc;

        @Mock
        private UserService userService;

        @InjectMocks
        private RegisterController registerController;

        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
            objectMapper = new ObjectMapper();
        }

        @Test
        void testSaveDetails_Success() throws Exception {
            RegisterDTO registerDTO = new RegisterDTO();
            registerDTO.setUsername("JohnDoe");
            registerDTO.setDepartment("password123");

            doNothing().when(userService).saveData(any(RegisterDTO.class));

            mockMvc.perform(post("/api/register/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("DATA SAVED SUCCESSFULLY"));

            verify(userService, times(1)).saveData(any(RegisterDTO.class));
        }

        @Test
        void testGetUserDetails_Success() throws Exception {
            Long userId = 1L;
            GetUserResponseDTO responseDTO = new GetUserResponseDTO();
            responseDTO.setId(userId);
            responseDTO.setUsername("JohnDoe");
            responseDTO.setDepartment("Engineering");

            when(userService.getRecordById(userId)).thenReturn(responseDTO);

            mockMvc.perform(get("/api/register/userDetails/{id}", userId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userId))
                    .andExpect(jsonPath("$.username").value("JohnDoe"))
                    .andExpect(jsonPath("$.department").value("Engineering"));

            verify(userService, times(1)).getRecordById(userId);
        }

        @Test
        void testDeleteUserDetails_Success() throws Exception {
            Long userId = 1L;

            doNothing().when(userService).deleteRecord(userId);

            mockMvc.perform(delete("/api/register/userDetails/{id}", userId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("DATA DELETED SUCCESFFULLY"));

            verify(userService, times(1)).deleteRecord(userId);
        }

}
