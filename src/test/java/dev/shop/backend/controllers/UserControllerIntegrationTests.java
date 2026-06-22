package dev.shop.backend.controllers;

import dev.shop.backend.TestDataUtilities;
import dev.shop.backend.domain.dto.UserDTO;
import dev.shop.backend.domain.entities.UserEntity;
import dev.shop.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
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
public class UserControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc, UserService userService){
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateUserReturnsHttpStatusCreated() throws Exception{

        UserEntity userEntityA = TestDataUtilities.createTestUserEntityA();

        String userJSON = objectMapper.writeValueAsString(userEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateUserReturnsSavedUser() throws Exception{

        UserEntity userEntityA = TestDataUtilities.createTestUserEntityA();

        String userJSON = objectMapper.writeValueAsString(userEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("testusera@testmail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("TestUserA")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.password").value("TestPassA")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.about").value("Test user A is a user.")
        );
    }

    @Test
    public void testThatFindAllUsersReturnsHttpStatusOK() throws Exception{

        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        userService.save(userA);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindAllUsersReturnsListOfUsers() throws Exception{

        UserEntity userA = TestDataUtilities.createTestUserEntityA();

        userService.save(userA);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").value("testusera@testmail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].username").value("TestUserA")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].password").value("TestPassA")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].about").value("Test user A is a user.")
        );
    }

    @Test
    public void testThatGetUserReturnsHttpStatusOK() throws Exception{

        UserEntity userA = TestDataUtilities.createTestUserEntityA();

        userService.save(userA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + userA.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetUserReturnsHttpStatusNotFoundWhenUserDoesNotExist() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/testusera@testmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetUserReturnsFoundUser() throws Exception{

        UserEntity userA = TestDataUtilities.createTestUserEntityA();

        userService.save(userA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + userA.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("testusera@testmail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("TestUserA")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.password").value("TestPassA")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.about").value("Test user A is a user.")
        );
    }

    @Test
    public void testThatPartialUpdateExistingUserReturnsHttpStatusOK() throws Exception{

        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        UserEntity savedUser = userService.save(userA);

        UserDTO userDTO = TestDataUtilities.createTestUserDTOA();
        userDTO.setUsername("UPDATED_USERNAME");

        String userJSON = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/" + userA.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateNotExistingUserReturnsHttpStatusNotFound() throws Exception{
        UserDTO userDTO = TestDataUtilities.createTestUserDTOA();
        userDTO.setUsername("UPDATED_USERNAME");

        String userJSON = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/" + userDTO.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateExistingUserReturnsUpdatedUser() throws Exception{
        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        UserEntity savedUser = userService.save(userA);

        UserDTO userDTO = TestDataUtilities.createTestUserDTOA();
        userDTO.setUsername("UPDATED_USERNAME");

        String userJSON = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/" + userA.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("testusera@testmail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("UPDATED_USERNAME")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.about").value(userA.getAbout())
        );
    }

    @Test
    public void testThatDeleteUserReturnsHttpStatusNoContentWhenUserDoesNotExist() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/random@testmail.com")
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteUserReturnsHttpStatusNoContentWhenUserExists() throws Exception{
        UserEntity userA = TestDataUtilities.createTestUserEntityA();
        userService.save(userA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/" + userA.getEmail())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
