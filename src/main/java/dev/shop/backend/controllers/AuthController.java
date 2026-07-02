package dev.shop.backend.controllers;

import dev.shop.backend.domain.dto.UserDTO;
import dev.shop.backend.domain.dto.security.AuthResponse;
import dev.shop.backend.domain.dto.security.LoginRequest;
import dev.shop.backend.domain.dto.security.RegisterRequest;
import dev.shop.backend.mappers.impl.UserMapper;
import dev.shop.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        UserDetails userDetails = authService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        String tokenValue = authService.generateToken(userDetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){

        UserDTO userDTO = UserDTO.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .role("USER")
                .password(request.getPassword())
                .about(null)
                .build();

        UserDetails userDetails = authService.register(userMapper.mapFrom(userDTO));
        String token = authService.generateToken(userDetails);

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .expiresIn(86400)
                .build();

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }
}
