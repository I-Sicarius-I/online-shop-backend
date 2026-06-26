package dev.shop.backend.service;

import dev.shop.backend.domain.entities.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderEntity save(OrderEntity orderEntity);

    List<OrderEntity> findAll();

    List<OrderEntity> findOrdersOfProduct(Long id);

    List<OrderEntity> findOrdersByUser(String email);

    Optional<OrderEntity> findOne(Long id);

    boolean isExists(Long id);

    OrderEntity partialUpdate(Long id, OrderEntity orderEntity);

    void delete(Long id);
}
