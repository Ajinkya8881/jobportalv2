package com.jobportal.jobportalv2.service;

import com.jobportal.jobportalv2.dto.ApplicationResponse;
import com.jobportal.jobportalv2.entity.Application;
import com.jobportal.jobportalv2.entity.Job;
import com.jobportal.jobportalv2.entity.User;
import com.jobportal.jobportalv2.exception.BadRequestException;
import com.jobportal.jobportalv2.repository.ApplicationRepository;
import com.jobportal.jobportalv2.repository.JobRepository;
import com.jobportal.jobportalv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationResponse applyToJob(Long jobId){

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email =  authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResolutionException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResolutionException("Job not found"));

        if(applicationRepository.existsByUserIdAndJobId(user.getId(), jobId)){
            throw new BadRequestException("Application already exists");
        }

        Application application = Application.builder()
                .user(user)
                .job(job)
                .appliedAt(LocalDateTime.now())
                .build();

        Application saved =  applicationRepository.save(application);

        return ApplicationResponse.builder()
                .message("Application submitted successfully")
                .id(saved.getId())
                .jobId(jobId)
                .jobTitle(job.getTitle())
                .appliedAt(LocalDateTime.now())
                .build();
    }



}
