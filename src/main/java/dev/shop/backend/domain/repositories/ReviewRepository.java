package dev.shop.backend.domain.repositories;

import dev.shop.backend.domain.entities.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
