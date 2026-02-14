package com.jobportal.jobportalv2.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateJobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Location is required")
    private String location;
}
