package com.jobportal.jobportalv2.event;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApplicationEvent {

    private String eventType;
    private Long jobId;
    private Long applicantId;
    private Long employerId;
    private String status;
}
