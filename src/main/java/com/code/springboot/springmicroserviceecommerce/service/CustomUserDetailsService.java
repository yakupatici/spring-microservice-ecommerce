package com.code.springboot.springmicroserviceecommerce.service;

import com.code.springboot.springmicroserviceecommerce.model.User;
import com.code.springboot.springmicroserviceecommerce.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map((role) -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()));
    }
} 