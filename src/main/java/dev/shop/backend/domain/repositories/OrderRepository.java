package dev.shop.backend.domain.repositories;

import dev.shop.backend.domain.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
