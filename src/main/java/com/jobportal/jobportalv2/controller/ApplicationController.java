package com.jobportal.jobportalv2.controller;

import com.jobportal.jobportalv2.dto.ApplicationResponse;
import com.jobportal.jobportalv2.dto.UpdateApplicationStatusRequest;
import com.jobportal.jobportalv2.entity.ApplicationStatus;
import com.jobportal.jobportalv2.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateApplicationStatusRequest request){
        applicationService.updateStatus(id, request.getStatus());
    }
}
