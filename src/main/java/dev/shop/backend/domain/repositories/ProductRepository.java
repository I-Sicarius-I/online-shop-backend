package dev.shop.backend.domain.repositories;

import dev.shop.backend.domain.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
