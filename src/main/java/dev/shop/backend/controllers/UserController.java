package dev.shop.backend.controllers;

import dev.shop.backend.domain.dto.UserDTO;
import dev.shop.backend.domain.entities.UserEntity;
import dev.shop.backend.mappers.Mapper;
import dev.shop.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController{

    private final UserService userService;

    private final Mapper<UserEntity, UserDTO> userMapper;

    public UserController(UserService userService, Mapper<UserEntity, UserDTO> userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
    }

//    @PostMapping("/users")
//    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userRequest){
//        UserEntity userEntity = userMapper.mapFrom(userRequest);
//        UserEntity savedUser = userService.save(userEntity);
//
//        return new ResponseEntity<>(userMapper.mapTo(savedUser), HttpStatus.CREATED);
//    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> listUsers(){

        List<UserEntity> users = userService.findAll();

        return new ResponseEntity<>(users
                .stream()
                .map(userMapper::mapTo)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String email){
        Optional<UserEntity> foundUser = userService.findOne(email);

        return foundUser.map(
                userEntity -> {
                    UserDTO userDTO = userMapper.mapTo(userEntity);
                    return new ResponseEntity<>(userDTO, HttpStatus.OK);
                }
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/users", params = "username")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam("username") String username){
        Optional<UserEntity> foundUser = userService.findByUsername(username);

        return foundUser.map(
                userEntity -> {
                    UserDTO userDTO = userMapper.mapTo(userEntity);
                    return new ResponseEntity<>(userDTO, HttpStatus.OK);
                }
        ).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/users/{email}")
    public ResponseEntity<UserDTO> partialUpdateUser(@PathVariable String email, @RequestBody UserDTO userDTO)
    {
        if(!userService.isExists(email))
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserEntity userEntity = userMapper.mapFrom(userDTO);
        UserEntity updatedUser = userService.partialUpdate(email, userEntity);

        return new ResponseEntity<>(userMapper.mapTo(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/users/{email}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String email)
    {
        userService.delete(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
