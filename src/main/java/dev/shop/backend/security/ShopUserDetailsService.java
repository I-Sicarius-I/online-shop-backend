package dev.shop.backend.security;

import dev.shop.backend.domain.entities.UserEntity;
import dev.shop.backend.domain.repositories.UserRepository;
import dev.shop.backend.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(email).orElseThrow(() -> new UsernameNotFoundException("User not found with matching username."));

        return new ShopUserDetail(user);
    }
}
