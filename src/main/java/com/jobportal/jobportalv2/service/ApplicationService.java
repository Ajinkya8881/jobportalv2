package com.jobportal.jobportalv2.service;

import com.jobportal.jobportalv2.dto.ApplicationResponse;
import com.jobportal.jobportalv2.entity.Application;
import com.jobportal.jobportalv2.entity.ApplicationStatus;
import com.jobportal.jobportalv2.entity.Job;
import com.jobportal.jobportalv2.entity.User;
import com.jobportal.jobportalv2.exception.BadRequestException;
import com.jobportal.jobportalv2.exception.ResourceNotFoundException;
import com.jobportal.jobportalv2.repository.ApplicationRepository;
import com.jobportal.jobportalv2.repository.JobRepository;
import com.jobportal.jobportalv2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import com.jobportal.jobportalv2.event.ApplicationEvent;
import com.jobportal.jobportalv2.event.ApplicationEventProducer;


@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationEventProducer eventProducer;

    public ApplicationResponse applyToJob(Long jobId){

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email =  authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if(applicationRepository.existsByUserIdAndJobId(user.getId(), jobId)){
            throw new BadRequestException("Application already exists");
        }

        Application application = Application.builder()
                .user(user)
                .job(job)
                .appliedAt(LocalDateTime.now())
                .status(ApplicationStatus.APPLIED)
                .build();

        Application saved =  applicationRepository.save(application);

        ApplicationEvent event = new ApplicationEvent(
                "APPLICATION_SUBMITTED",
                job.getId(),
                user.getId(),
                job.getEmployer().getId(),
                null
        );

        eventProducer.publishEvent(event);

        return ApplicationResponse.builder()
                .message("Application submitted successfully")
                .id(saved.getId())
                .jobId(jobId)
                .jobTitle(job.getTitle())
                .appliedAt(saved.getAppliedAt())
                .build();
    }

    public void updateStatus(Long applicationId, ApplicationStatus status) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(()-> new BadRequestException("Application not found"));

        application.setStatus(status);
        Application saved = applicationRepository.save(application);

        ApplicationEvent event = new ApplicationEvent(
                "APPLICATION_STATUS_UPDATED",
                saved.getJob().getId(),
                saved.getUser().getId(),
                saved.getJob().getEmployer().getId(),
                status.name()
        );

        eventProducer.publishEvent(event);
    }



}
