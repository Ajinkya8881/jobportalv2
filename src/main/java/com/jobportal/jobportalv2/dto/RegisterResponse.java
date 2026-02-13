package com.jobportal.jobportalv2.dto;


import lombok.Getter;
import lombok.Builder;

@Builder
@Getter
public class RegisterResponse {
    private Long id;
    private String name;
    private String email;
}
