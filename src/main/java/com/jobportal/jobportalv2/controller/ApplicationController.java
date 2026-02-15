package com.jobportal.jobportalv2.controller;

import com.jobportal.jobportalv2.dto.ApplicationResponse;
import com.jobportal.jobportalv2.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;


    @PostMapping("/{jobId}")
    @PreAuthorize("hasRole('USER')")
    public ApplicationResponse apply(@PathVariable Long jobId){
        return applicationService.applyToJob(jobId);
    }
}
