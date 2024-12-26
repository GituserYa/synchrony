package com.example.synchrony;

import com.example.synchrony.entity.Image;
import com.example.synchrony.entity.User;
import com.example.synchrony.repository.ImageRepository;
import com.example.synchrony.repository.UserRepository;
import com.example.synchrony.service.ImageServiceImpl;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class SynchronyApplicationTests {

    @Test
    void contextLoads() {
    }

    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageServiceImpl serviceImpl;
    @MockBean
    private ProducerTemplate producerTemplate;
    @Mock
    private ImageRepository imageRepository;
    @MockBean
    private CamelContext camelContext;
    @Mock
    private RestTemplate restTemplate;
    @Captor
    ArgumentCaptor<HttpEntity<?>> requestEntityCaptor;

    @InjectMocks
    private ImageServiceImpl imageService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        CamelContext mockCamelContext = mock(CamelContext.class);
        ReflectionTestUtils.setField(imageService, "camelContext", mockCamelContext);
    }

    private byte[] createDummyImageContent() throws IOException {
        // Create a dummy image (you can customize the dimensions and format)
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        // Write the image to a ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        // Get the byte array from the ByteArrayOutputStream
        return byteArrayOutputStream.toByteArray();
    }


    @Test
    void uploadImageToImgur_Success() throws Exception {
        // Mocking user details
        var user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        // Create dummy image content
        byte[] dummyImageContent = createDummyImageContent();

        // Mocking file
        MockMultipartFile file = new MockMultipartFile(
                "file", "test-image.png", "image/png", dummyImageContent);

        // Mocking userRepository response
        when(userRepository.findUserByNameAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(user));

        // Mocking imageRepository save method
        when(imageRepository.save(any(Image.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // Performing the test
        ResponseEntity<String> responseEntity = imageService.uploadImageToImgur(file, "testUser", "testPassword");

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}



