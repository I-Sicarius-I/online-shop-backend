package dev.shop.backend;

import dev.shop.backend.domain.dto.ProductDTO;
import dev.shop.backend.domain.dto.UserDTO;
import dev.shop.backend.domain.entities.ProductEntity;
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

    public static ProductEntity createProductEntityA(final UserEntity userEntity){
        return ProductEntity.builder()
                .name("Test product A")
                .type("Test")
                .state("New")
                .quantity(25L)
                .description("A test product, brand new")
                .price(123.)
                .rating(10.)
                .seller(userEntity)
                .build();
    }

    public static ProductDTO createProductDTOA(final UserDTO userDTO){
        return ProductDTO.builder()
                .name("Test product A")
                .type("Test")
                .state("New")
                .quantity(25L)
                .description("A test product, brand new")
                .price(123.)
                .rating(10.)
                .seller(userDTO)
                .build();
    }
}
