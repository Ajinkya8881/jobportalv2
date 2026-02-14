package com.jobportal.jobportalv2.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.jobportalv2.exception.ApiError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;


@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override

    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    )throws IOException, ServletException{

        ApiError error = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .message("Access Denied")
                .path(request.getRequestURI())
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }


}
