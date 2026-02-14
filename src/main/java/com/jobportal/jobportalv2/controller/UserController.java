package com.jobportal.jobportalv2.controller;

import com.jobportal.jobportalv2.dto.LoginRequest;
import com.jobportal.jobportalv2.dto.LoginResponse;
import com.jobportal.jobportalv2.dto.RegisterRequest;
import com.jobportal.jobportalv2.dto.RegisterResponse;
import com.jobportal.jobportalv2.entity.User;
import com.jobportal.jobportalv2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController( UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponse register( @Valid @RequestBody RegisterRequest request) {

        RegisterResponse user = userService.register(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );

        return RegisterResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request.getEmail(), request.getPassword());
    }


}
