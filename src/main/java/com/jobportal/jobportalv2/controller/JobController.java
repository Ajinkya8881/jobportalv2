package com.jobportal.jobportalv2.controller;

import com.jobportal.jobportalv2.dto.CreateJobRequest;
import com.jobportal.jobportalv2.dto.JobResponse;
import com.jobportal.jobportalv2.dto.PaginatedResponse;
import com.jobportal.jobportalv2.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public JobResponse createJob(@Valid @RequestBody CreateJobRequest request) {
        return jobService.createJob(request);
    }

    @GetMapping
    public PaginatedResponse<JobResponse> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return jobService.getAllJobs(page,size,sortBy, direction);

    }
}
