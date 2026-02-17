Job Portal Backend (Monolith + Kafka Integration)

A production-style Spring Boot backend system implementing a secure Job Portal with JWT authentication, role-based authorization, pagination, validation, Docker-based infrastructure, and Kafka-based event publishing integrated with a separate Notification microservice.

This project demonstrates real-world backend architecture used in production-grade systems.

🚀 Tech Stack

Java 17

Spring Boot 3.x

Spring Security (JWT Authentication)

Spring Data JPA (Hibernate)

MySQL 8

Apache Kafka

Docker & Docker Compose

Lombok

🏗 Architecture

Layered Architecture:

Controller → Service → Repository → Database

Core Layers

Controller → Handles HTTP requests

Service → Business logic

Repository → JPA database interaction

DTO → Request/Response isolation

Entity → Database mapping

Security → JWT + custom handlers

Event Layer → Kafka Producer

Exception Layer → Global exception handling

🔐 Security Features

Stateless authentication

JWT token generation

BCrypt password hashing

Custom 401 Authentication EntryPoint

Custom 403 AccessDeniedHandler

Role-based endpoint restriction

Method-level security using @PreAuthorize

spring.jpa.open-in-view=false (Production optimization)

👥 Roles
Role	Access
USER	Apply to jobs
ADMIN	Create jobs, update application status

Admin auto-created at startup:

Email: admin@jobportal.com

Password: admin123
Role: ROLE_ADMIN

🗄 Database Design
Users

Unique email constraint

BCrypt password stored

Role stored as string

Jobs

Linked to employer (ManyToOne)

Created timestamp

Pagination enabled

Applications

Unique constraint on (user_id, job_id)

Status enum:

APPLIED

ACCEPTED

REJECTED

📨 Kafka Integration

Events are published when:

A user applies to a job

An admin updates application status

Topic:

application-events

Example Event Payload:

{
"eventType": "APPLICATION_STATUS_UPDATED",
"jobId": 1,
"applicantId": 3,
"employerId": 1,
"status": "ACCEPTED"
}


Notification Service consumes this event and persists notification independently.

🐳 Running the Project
Option 1 — Run Infrastructure with Docker (Recommended)

Start MySQL + Kafka + Zookeeper:

docker-compose up -d mysql kafka zookeeper


MySQL runs on port 3307
Kafka runs on port 9092

Then run Spring Boot:

./mvnw spring-boot:run


App runs at:

http://localhost:8080

Option 2 — Run Full Docker (App + Infra)
docker-compose up -d


App: http://localhost:8080

MySQL: 3307
Kafka: 9092

🧪 COMPLETE END-TO-END TEST FLOW
1️⃣ Register User

POST
http://localhost:8080/users/register

Body:

{
"name": "Ajinkya",
"email": "user@test.com",
"password": "123456"
}

2️⃣ Login

POST
http://localhost:8080/users/login

Body:

{
"email": "user@test.com",
"password": "123456"
}


Response:

{
"token": "JWT_TOKEN"
}


Copy this token.

3️⃣ Login as Admin

POST
http://localhost:8080/users/login

Body:

{
"email": "admin@jobportal.com",
"password": "admin123"
}


Copy ADMIN token.

4️⃣ Create Job (ADMIN Only)

POST
http://localhost:8080/jobs

Header:

Authorization: Bearer <ADMIN_TOKEN>

Body:

{
"title": "Spring Boot Developer",
"description": "Backend + Kafka",
"company": "TechCorp",
"location": "Pune"
}

5️⃣ View Jobs (Public)

GET
http://localhost:8080/jobs?page=0&size=5&sortBy=createdAt&direction=desc

6️⃣ Apply to Job (USER Only)

POST
http://localhost:8080/applications/1

Header:

Authorization: Bearer <USER_TOKEN>

Response:

{
"message": "Application submitted successfully",
"id": 1,
"jobId": 1,
"jobTitle": "Spring Boot Developer",
"appliedAt": "2026-02-17T13:39:41"
}


Kafka event published.

7️⃣ Update Application Status (ADMIN Only)

PATCH
http://localhost:8080/applications/1/status

Header:

Authorization: Bearer <ADMIN_TOKEN>

Body:

{
"status": "ACCEPTED"
}


Kafka event published.

Application status updated in jobportal database.

🔔 Notification Microservice

Runs separately on:

http://localhost:8081


Consumes Kafka events and stores notifications in:

notification_db

8️⃣ Check Notifications

GET
http://localhost:8081/notifications

Example Response:

[
{
"id": 3,
"jobId": 1,
"applicantId": 3,
"employerId": 1,
"eventType": "APPLICATION_STATUS_UPDATED",
"status": "ACCEPTED",
"createdAt": "2026-02-17T15:10:11",
"read": false
}
]


This confirms event-driven integration is working.

9️⃣ Mark Notification As Read

PATCH
http://localhost:8081/notifications/3/read

No body required.

After this:

GET
http://localhost:8081/notifications

Now:

"read": true

🧠 Production Decisions Implemented

DTO isolation (no entity leakage)

Sort field whitelist (prevent sort injection)

Global exception handling

Role-based access control

Unique application prevention

Stateless security

Open-In-View disabled

Event-driven decoupling

Microservice-ready architecture

📦 Microservice Design

Monolith publishes domain events.
Notification service consumes events independently.

Benefits:

Service decoupling

Horizontal scalability

Future extension (Email/SMS/Push)

Clean domain separation

📈 What This Project Demonstrates

Secure backend development

JWT-based authentication

Production-style layered architecture

Event-driven system design

Kafka integration

Database constraint management

Role-based authorization

Clean error handling

Docker-based infrastructure setup

Repository Structure
jobportalv2 (Monolith)

Controllers

Services

Repositories

DTOs

Entities

Security Layer

Kafka Producer

Global Exception Handler

Docker configuration

notification-service (Microservice)

Kafka Consumer

Notification persistence

DTO mapping

Read/unread tracking

Independent database

Open-in-view disabled

Runs on port 8081