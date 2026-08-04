package dev.shop.backend.domain.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// make username an id instead of user email
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
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

    @Builder.Default
    @OneToMany(mappedBy = "sellerId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "reviewerId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviewEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "buyerId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orderEntities = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(fname, user.fname) && Objects.equals(lname, user.lname) && Objects.equals(address, user.address) && Objects.equals(city, user.city) && Objects.equals(code, user.code) && Objects.equals(role, user.role) && Objects.equals(password, user.password) && Objects.equals(about, user.about);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username, fname, lname, address, city, code, role, password, about);
    }
}
