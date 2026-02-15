package com.jobportal.jobportalv2.repository;

import com.jobportal.jobportalv2.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByUserIdAndJobId(Long userId, Long jobId);
}
