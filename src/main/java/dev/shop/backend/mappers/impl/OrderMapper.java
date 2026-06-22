package dev.shop.backend.mappers.impl;

import dev.shop.backend.domain.dto.OrderDTO;
import dev.shop.backend.domain.entities.OrderEntity;
import dev.shop.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements Mapper<OrderEntity, OrderDTO> {

    private final ModelMapper modelMapper;

    public OrderMapper(ModelMapper modelMapper){ this.modelMapper = modelMapper;}

    @Override
    public OrderDTO mapTo(OrderEntity orderEntity){
        return modelMapper.map(orderEntity, OrderDTO.class);
    }

    @Override
    public OrderEntity mapFrom(OrderDTO orderDTO){
        return modelMapper.map(orderDTO, OrderEntity.class);
    }

}
