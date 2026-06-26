package dev.shop.backend.domain.repositories;

import dev.shop.backend.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    public boolean existsByUsername(String username);

    public Optional<UserEntity> findUserByUsername(String username);
}
