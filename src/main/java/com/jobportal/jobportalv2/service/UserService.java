package com.jobportal.jobportalv2.service;

import com.jobportal.jobportalv2.dto.LoginResponse;
import com.jobportal.jobportalv2.dto.RegisterResponse;
import com.jobportal.jobportalv2.entity.User;
import com.jobportal.jobportalv2.exception.BadRequestException;
import com.jobportal.jobportalv2.exception.ResourceNotFoundException;
import com.jobportal.jobportalv2.exception.UnauthorizedException;
import com.jobportal.jobportalv2.repository.UserRepository;
import com.jobportal.jobportalv2.security.JwtUtil;
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

    public RegisterResponse register(String name, String email, String password){

        if(userRepository.existsByEmail(email)){
            throw new BadRequestException("Email already exists");
        }
        String encodePassword = passwordEncoder.encode(password);

        User user = User.builder()
                .name(name)
                .email(email)
                .password(encodePassword)
                .role("ROLE_USER")
                .build();

        User saved =  userRepository.save(user);

        return RegisterResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .build();
    }
    public LoginResponse login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new UnauthorizedException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(email);

        return LoginResponse.builder()
                .token(token)
                .build();
    }


}
