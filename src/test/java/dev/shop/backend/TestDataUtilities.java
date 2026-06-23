package dev.shop.backend;

import dev.shop.backend.domain.dto.OrderDTO;
import dev.shop.backend.domain.dto.ProductDTO;
import dev.shop.backend.domain.dto.ReviewDTO;
import dev.shop.backend.domain.dto.UserDTO;
import dev.shop.backend.domain.entities.OrderEntity;
import dev.shop.backend.domain.entities.ProductEntity;
import dev.shop.backend.domain.entities.ReviewEntity;
import dev.shop.backend.domain.entities.UserEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public final class TestDataUtilities {

    private static final DateFormat formatter = new SimpleDateFormat("dd.mm.yyyy");
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

    public static OrderEntity createOrderEntityA(final UserEntity userEntity, final ProductEntity productEntity){
        Instant baseDate = Instant.parse("2026-06-23T10:00:00Z");

        Date dateOrdered = Date.from(baseDate.minus(3, ChronoUnit.DAYS));
        Date dateShipped = Date.from(baseDate.minus(2, ChronoUnit.DAYS));
        Date dateReceived = Date.from(baseDate);

        return OrderEntity.builder()
                .quantity(25L)
                .dateOrdered(dateOrdered)
                .dateShipped(dateShipped)
                .dateReceived(dateReceived)
                .buyer(userEntity)
                .product(productEntity)
                .build();
    }

    public static OrderDTO createOrderDTOA(final UserDTO userDTO, final ProductDTO productDTO){
        Instant baseDate = Instant.parse("2026-06-23T10:00:00Z");

        Date dateOrdered = Date.from(baseDate.minus(3, ChronoUnit.DAYS));
        Date dateShipped = Date.from(baseDate.minus(2, ChronoUnit.DAYS));
        Date dateReceived = Date.from(baseDate);

        return OrderDTO.builder()
                .quantity(25L)
                .dateOrdered(dateOrdered)
                .dateShipped(dateShipped)
                .dateReceived(dateReceived)
                .buyer(userDTO)
                .product(productDTO)
                .build();
    }

    public static ReviewEntity createReviewEntityA(final UserEntity userEntity, final ProductEntity productEntity){
        return ReviewEntity.builder()
                .text("Good test product")
                .rating(10)
                .reviewer(userEntity)
                .product(productEntity)
                .build();
    }

    public static ReviewDTO createReviewDTOA(final UserDTO userDTO, final ProductDTO productDTO){
        return ReviewDTO.builder()
                .text("Good test product")
                .rating(10)
                .reviewer(userDTO)
                .product(productDTO)
                .build();
    }
}
