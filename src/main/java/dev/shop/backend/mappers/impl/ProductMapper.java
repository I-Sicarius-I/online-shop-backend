package dev.shop.backend.mappers.impl;

import dev.shop.backend.domain.dto.ProductDTO;
import dev.shop.backend.domain.entities.ProductEntity;
import dev.shop.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements Mapper<ProductEntity, ProductDTO> {

    private final ModelMapper modelMapper;

    public ProductMapper(ModelMapper modelMapper){ this.modelMapper = modelMapper;}

    @Override
    public ProductDTO mapTo(ProductEntity productEntity){
        return modelMapper.map(productEntity, ProductDTO.class);
    }

    @Override
    public ProductEntity mapFrom(ProductDTO productDTO){
        return modelMapper.map(productDTO, ProductEntity.class);
    }
}
