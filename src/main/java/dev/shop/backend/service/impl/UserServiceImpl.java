package dev.shop.backend.service.impl;

import dev.shop.backend.domain.entities.UserEntity;
import dev.shop.backend.domain.repositories.UserRepository;
import dev.shop.backend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity save(UserEntity userEntity){
        return userRepository.save(userEntity);
    }

    @Override
    public List<UserEntity> findAll(){
        return StreamSupport.stream(
                userRepository
                        .findAll()
                        .spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserEntity> findOne(String email){
        return userRepository.findById(email);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username){ return userRepository.findUserByUsername(username);}

    @Override
    public boolean isExists(String email){
        return userRepository.existsById(email);
    }

    @Override
    public boolean isExistsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserEntity partialUpdate(String email, UserEntity userEntity){
        userEntity.setEmail(email);

        return userRepository.findById(email).map(
                existingUser -> {
                    Optional.ofNullable(userEntity.getUsername()).ifPresent(existingUser::setUsername);
                    Optional.ofNullable(userEntity.getAbout()).ifPresent(existingUser::setAbout);
                    return userRepository.save(existingUser);
                }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void delete(String email){
        userRepository.deleteById(email);
    }
}
