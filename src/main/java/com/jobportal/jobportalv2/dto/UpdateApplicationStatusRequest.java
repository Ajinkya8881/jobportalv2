package com.jobportal.jobportalv2.dto;


import com.jobportal.jobportalv2.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateApplicationStatusRequest {
    @NotNull
    private ApplicationStatus status;
}
