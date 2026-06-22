package dev.shop.backend.mappers.impl;

import dev.shop.backend.domain.dto.UserDTO;
import dev.shop.backend.domain.entities.UserEntity;
import dev.shop.backend.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserEntity, UserDTO> {

    private ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper){ this.modelMapper = modelMapper;}

    @Override
    public UserDTO mapTo(UserEntity userEntity){
        return modelMapper.map(userEntity, UserDTO.class);
    }

    @Override
    public UserEntity mapFrom(UserDTO userDTO){
        return modelMapper.map(userDTO, UserEntity.class);
    }

}
