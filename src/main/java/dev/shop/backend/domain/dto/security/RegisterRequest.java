package dev.shop.backend.domain.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String email;

    private String fname;

    private String lname;

    private String address;

    private String city;

    private String code;

    private String username;

    private String password;
}
