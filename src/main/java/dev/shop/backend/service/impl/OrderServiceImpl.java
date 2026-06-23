package dev.shop.backend.service.impl;

import dev.shop.backend.domain.entities.OrderEntity;
import dev.shop.backend.domain.repositories.OrderRepository;
import dev.shop.backend.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderEntity save(OrderEntity orderEntity){
        return orderRepository.save(orderEntity);
    }

    @Override
    public List<OrderEntity> findAll(){
        return StreamSupport.stream(
                orderRepository
                        .findAll()
                        .spliterator(), false)
                .collect(Collectors.toList()
        );
    }

    @Override
    public Optional<OrderEntity> findOne(Long id){
        return orderRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id){
        return orderRepository.existsById(id);
    }

    @Override
    public OrderEntity partialUpdate(Long id, OrderEntity orderEntity){
        orderEntity.setId(id);

        return orderRepository.findById(id).map(
                existingOrder -> {
                    Optional.ofNullable(orderEntity.getQuantity()).ifPresent(existingOrder::setQuantity);
                    Optional.ofNullable(orderEntity.getDateOrdered()).ifPresent(existingOrder::setDateOrdered);
                    Optional.ofNullable(orderEntity.getDateOrdered()).ifPresent(existingOrder::setDateOrdered);
                    Optional.ofNullable(orderEntity.getDateReceived()).ifPresent(existingOrder::setDateReceived);
                    return orderRepository.save(existingOrder);
                }
        ).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public void delete(Long id){
        orderRepository.deleteById(id);
    }
}
