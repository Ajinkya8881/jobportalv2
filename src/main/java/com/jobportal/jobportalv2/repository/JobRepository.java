package com.jobportal.jobportalv2.repository;

import com.jobportal.jobportalv2.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {


}
