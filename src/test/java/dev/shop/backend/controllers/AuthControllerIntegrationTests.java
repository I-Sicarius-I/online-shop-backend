package dev.shop.backend.controllers;

import dev.shop.backend.TestDataUtilities;
import dev.shop.backend.domain.dto.security.LoginRequest;
import dev.shop.backend.domain.dto.security.RegisterRequest;
import dev.shop.backend.domain.entities.UserEntity;
import dev.shop.backend.security.ShopUserDetail;
import dev.shop.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthService authService;

    @Autowired
    public AuthControllerIntegrationTests(MockMvc mockMvc, AuthService authService){
        this.mockMvc = mockMvc;
        this.authService = authService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatRegisterNewUserReturnsHttpStatusOK() throws Exception{

        RegisterRequest request = TestDataUtilities.createRegisterRequest();

        String requestJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatRegisterNewUserReturnsToken() throws Exception{

        RegisterRequest request = TestDataUtilities.createRegisterRequest();

        String requestJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("token").hasJsonPath()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("token").isNotEmpty()
        );
    }

    @Test
    public void testThatRegisterExistingUserReturnsHttpStatusUnauthorized() throws Exception{

        UserEntity testUser = TestDataUtilities.createTestUserEntityA();
        authService.register(testUser);

        RegisterRequest request = TestDataUtilities.createRegisterRequest();
        request.setEmail(testUser.getEmail());

        String requestJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }

    @Test
    public void testThatRegisterExistingUserReturnsErrorMessage() throws Exception{

        UserEntity testUser = TestDataUtilities.createTestUserEntityA();
        authService.register(testUser);

        RegisterRequest request = TestDataUtilities.createRegisterRequest();
        request.setEmail(testUser.getEmail());

        String requestJSON = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("status").value(401)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("message").value("User with provided email or username already exists.")
        );
    }

    @Test
    public void testThatLoggingInAsExistingUserReturnsHttpStatusOK() throws Exception{

        UserEntity testUser = TestDataUtilities.createTestUserEntityA();

        UserDetails user = authService.register(testUser);

        System.out.println(user);

        LoginRequest loginRequest = LoginRequest.builder()
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .build();


        String loginJSON = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatLoggingInAsExistingUserReturnsToken() throws Exception{
        UserEntity testUser = TestDataUtilities.createTestUserEntityA();

        authService.register(testUser);

        LoginRequest loginRequest = LoginRequest.builder()
                .email(testUser.getEmail())
                .password(testUser.getPassword())
                .build();


        String loginJSON = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("token").hasJsonPath()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("token").isNotEmpty()
        );
    }

    @Test
    public void testThatLoggingAsNonExistingUserReturnsHttpStatusUnauthorized() throws Exception{

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@email.com")
                .password("1234124")
                .build();

        String loginJSON = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }

    @Test
    public void testThatLoggingAsNonExistingUserReturnsErrorMessage() throws Exception{

        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@email.com")
                .password("1234124")
                .build();

        String loginJSON = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("status").value(401)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("message").isNotEmpty()
        );
    }

}
