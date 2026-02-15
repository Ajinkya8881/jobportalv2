package com.jobportal.jobportalv2.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApplicationResponse {

    private String message;

    private Long id;
    private Long jobId;
    private String jobTitle;
    private LocalDateTime appliedAt;

}
