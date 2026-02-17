package com.jobportal.jobportalv2.service;

import com.jobportal.jobportalv2.dto.CreateJobRequest;
import com.jobportal.jobportalv2.dto.JobResponse;
import com.jobportal.jobportalv2.dto.PaginatedResponse;
import com.jobportal.jobportalv2.entity.Job;
import com.jobportal.jobportalv2.exception.BadRequestException;
import com.jobportal.jobportalv2.exception.ResourceNotFoundException;
import com.jobportal.jobportalv2.repository.JobRepository;
import com.jobportal.jobportalv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.jobportal.jobportalv2.entity.User;


import org.springframework.data.domain.Pageable;



@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    private static final List<String> ALLOWED_SORT_FIELDS =
            List.of("title", "company", "location", "createdAt");

    public JobResponse createJob(CreateJobRequest request) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email =  authentication.getName();

        User employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .company(request.getCompany())
                .location(request.getLocation())
                .createdAt(LocalDateTime.now())
                .employer(employer)
                .build();

        Job saved =  jobRepository.save(job);

        return mapToResponse(saved);

    }

    public PaginatedResponse<JobResponse> getAllJobs(int page,
                                                     int size,
                                                     String sortBy,
                                                     String direction) {

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)){
            throw new BadRequestException("Invalid sort fields: " + sortBy);
        }

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Job> jobPage = jobRepository.findAll(pageable);

        List<JobResponse> content = jobPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return PaginatedResponse.<JobResponse>builder()
                .content(content)
                .page(jobPage.getNumber())
                .size(jobPage.getSize())
                .totalElements(jobPage.getTotalElements())
                .totalPages(jobPage.getTotalPages())
                .last(jobPage.isLast())
                .build();
    }

    private JobResponse mapToResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .company(job.getCompany())
                .location(job.getLocation())
                .createdAt(job.getCreatedAt())
                .build();
    }
}
