package dev.shop.backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String email;

    private String username;

    private String fname;

    private String lname;

    private String address;

    private String city;

    private String code;

    private String role;

    private String password;

    private String about;
}
