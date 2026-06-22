package dev.shop.backend.mappers.impl;

import dev.shop.backend.domain.dto.ReviewDTO;
import dev.shop.backend.domain.entities.ReviewEntity;
import dev.shop.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper implements Mapper<ReviewEntity, ReviewDTO> {

    private final ModelMapper modelMapper;

    public ReviewMapper(ModelMapper modelMapper){ this.modelMapper = modelMapper;}

    @Override
    public ReviewDTO mapTo(ReviewEntity reviewEntity){
        return modelMapper.map(reviewEntity, ReviewDTO.class);
    }

    @Override
    public ReviewEntity mapFrom(ReviewDTO reviewDTO){
        return modelMapper.map(reviewDTO, ReviewEntity.class);
    }
}
