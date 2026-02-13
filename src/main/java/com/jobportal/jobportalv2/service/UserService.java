package com.jobportal.jobportalv2.service;

import com.jobportal.jobportalv2.entity.User;
import com.jobportal.jobportalv2.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String name, String email, String password){

        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("Email already exists");
        }
        String encodePassword = passwordEncoder.encode(password);

        User user = User.builder()
                .name(name)
                .email(email)
                .password(encodePassword)
                .role("ROLE_USER")
                .build();

        return userRepository.save(user);
    }


}
