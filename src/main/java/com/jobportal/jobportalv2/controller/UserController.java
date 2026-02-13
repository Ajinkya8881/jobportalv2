package com.jobportal.jobportalv2.controller;

import com.jobportal.jobportalv2.dto.RegisterRequest;
import com.jobportal.jobportalv2.dto.RegisterResponse;
import com.jobportal.jobportalv2.entity.User;
import com.jobportal.jobportalv2.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController( UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {

        User user = userService.register(
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


}
