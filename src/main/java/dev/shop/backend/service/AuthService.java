package dev.shop.backend.service;


import dev.shop.backend.domain.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    UserDetails authenticate(String email, String password);

    String generateToken(UserDetails userDetails);

    UserDetails validateToken(String token);

    UserDetails register(UserEntity user);
}
