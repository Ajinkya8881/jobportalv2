package com.jobportal.jobportalv2.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.jobportalv2.exception.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
            )throws IOException, ServletException{

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
            .status(HttpStatus.UNAUTHORIZED.value())
                .message("Authentication required")
                .path(request.getRequestURI())
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getOutputStream(),error);
    }
}
