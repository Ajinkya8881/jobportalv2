package com.jobportal.jobportalv2.service;

import com.jobportal.jobportalv2.dto.CreateJobRequest;
import com.jobportal.jobportalv2.dto.JobResponse;
import com.jobportal.jobportalv2.entity.Job;
import com.jobportal.jobportalv2.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public JobResponse createJob(CreateJobRequest request) {
        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .company(request.getCompany())
                .location(request.getLocation())
                .createdAt(LocalDateTime.now())
                .build();

        Job saved =  jobRepository.save(job);

        return mapToResponse(saved);

    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
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
