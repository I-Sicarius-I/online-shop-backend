package dev.shop.backend.domain.repositories;

import dev.shop.backend.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    public boolean existsByUsername(String username);
}
