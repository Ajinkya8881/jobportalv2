package com.jobportal.jobportalv2.service;

import com.jobportal.jobportalv2.dto.LoginResponse;
import com.jobportal.jobportalv2.entity.User;
import com.jobportal.jobportalv2.repository.UserRepository;
import com.jobportal.jobportalv2.security.JwtUtil;
import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
    public LoginResponse login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Wrong password");
        }
        String token = jwtUtil.generateToken(email);

        return LoginResponse.builder()
                .token(token)
                .build();
    }


}
