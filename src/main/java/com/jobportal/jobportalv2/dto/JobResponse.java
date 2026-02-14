package com.jobportal.jobportalv2.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String company;
    private String location;
    private LocalDateTime createdAt;
}
