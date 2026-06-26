package dev.shop.backend.service;

import dev.shop.backend.domain.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserEntity save(UserEntity userEntity);

    List<UserEntity> findAll();

    Optional<UserEntity> findOne(String email);

    Optional<UserEntity> findByUsername(String username);

    boolean isExists(String email);

    boolean isExistsByUsername(String email);

    UserEntity partialUpdate(String email, UserEntity userEntity);

    void delete(String email);
}
