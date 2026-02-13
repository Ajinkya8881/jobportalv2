package com.jobportal.jobportalv2.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Builder;

@Data
@Builder
public class RegisterResponse {
    private Long id;
    private String name;
    private String email;
}
