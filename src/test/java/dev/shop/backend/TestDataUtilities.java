package dev.shop.backend;

import dev.shop.backend.domain.dto.UserDTO;
import dev.shop.backend.domain.entities.UserEntity;

public final class TestDataUtilities {

    private TestDataUtilities(){}

    public static UserEntity createTestUserEntityA(){
        return UserEntity.builder()
                .email("testusera@testmail.com")
                .username("TestUserA")
                .role("User")
                .password("TestPassA")
                .about("Test user A is a user.")
                .build();
    }

    public static UserDTO createTestUserDTOA(){
        return UserDTO.builder()
                .email("testusera@testmail.com")
                .username("TestUserA")
                .role("User")
                .password("TestPassA")
                .about("Test user A is a user.")
                .build();
    }


    public static UserEntity createTestUserEntityB(){
        return UserEntity.builder()
                .email("testuserb@testmail.com")
                .username("TestUserB")
                .role("User")
                .password("TestPassB")
                .about("Test user B is a user.")
                .build();
    }

    public static UserDTO createTestUserDTOB(){
        return UserDTO.builder()
                .email("testuserb@testmail.com")
                .username("TestUserB")
                .role("User")
                .password("TestPassB")
                .about("Test user B is a user.")
                .build();
    }


    public static UserEntity createTestUserEntityC(){
        return UserEntity.builder()
                .email("testuserc@testmail.com")
                .username("TestUserC")
                .role("User")
                .password("TestPassC")
                .about("Test user C is a user.")
                .build();
    }

    public static UserDTO createTestUserDTOC(){
        return UserDTO.builder()
                .email("testuserc@testmail.com")
                .username("TestUserC")
                .role("User")
                .password("TestPassC")
                .about("Test user C is a user.")
                .build();
    }
}
