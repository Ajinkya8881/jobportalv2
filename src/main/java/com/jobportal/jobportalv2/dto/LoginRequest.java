package com.jobportal.jobportalv2.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
