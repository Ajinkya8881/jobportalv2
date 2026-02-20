# Job Portal Backend (Monolith + Kafka Integration)

Backend system implementing a secure Job Portal using Spring Boot with JWT-based authentication, role-based authorization, pagination, validation, Docker-based infrastructure, and Kafka-based event publishing integrated with a separate Notification microservice.

---

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security (JWT Authentication)
- Spring Data JPA (Hibernate)
- MySQL 8
- Apache Kafka
- Docker & Docker Compose
- Lombok

---

## Architecture

Layered Architecture:

Controller → Service → Repository → Database

Core Layers:

- Controller – Handles HTTP requests
- Service – Business logic
- Repository – JPA database interaction
- DTO – Request/Response isolation
- Entity – Database mapping
- Security – JWT authentication & authorization
- Event Layer – Kafka Producer
- Exception Layer – Global exception handling

---

## Security

- Stateless authentication
- JWT token generation
- BCrypt password hashing
- Custom 401 AuthenticationEntryPoint
- Custom 403 AccessDeniedHandler
- Role-based endpoint restriction
- Method-level security using `@PreAuthorize`
- `spring.jpa.open-in-view=false`

---

## Roles

| Role  | Access |
|-------|--------|
| USER  | Apply to jobs |
| ADMIN | Create jobs, update application status |

Admin auto-created at startup:

- Email: `admin@jobportal.com`
- Password: `admin123`
- Role: `ROLE_ADMIN`

---

## Database Design

### Users

- Unique email constraint
- BCrypt password storage
- Role stored as string

### Jobs

- Linked to employer (ManyToOne)
- Created timestamp
- Pagination enabled

### Applications

- Unique constraint on `(user_id, job_id)`

Status enum:

- APPLIED
- ACCEPTED
- REJECTED

---

## Kafka Integration

Events are published when:

- A user applies to a job
- An admin updates application status

Topic:

application-events


Example event payload:

```json
{
  "eventType": "APPLICATION_STATUS_UPDATED",
  "jobId": 1,
  "applicantId": 3,
  "employerId": 1,
  "status": "ACCEPTED"
}
Notification service consumes these events and persists notifications independently.

Running the Project
Option 1 — Run Infrastructure Only
Start MySQL, Kafka and Zookeeper:

docker-compose up -d mysql kafka zookeeper
MySQL: port 3307
Kafka: port 9092

Then start Spring Boot:

./mvnw spring-boot:run
Application runs at:

http://localhost:8080
Option 2 — Run Full Docker Setup
docker-compose up -d
Application: http://localhost:8080
MySQL: 3307
Kafka: 9092

End-to-End Test Flow
1. Register User
POST
http://localhost:8080/users/register

{
  "name": "Ajinkya",
  "email": "user@test.com",
  "password": "123456"
}
2. Login
POST
http://localhost:8080/users/login

{
  "email": "user@test.com",
  "password": "123456"
}
Response:

{
  "token": "JWT_TOKEN"
}
Copy this token.

3. Login as Admin
POST
http://localhost:8080/users/login

{
  "email": "admin@jobportal.com",
  "password": "admin123"
}
Copy ADMIN token.

4. Create Job (ADMIN Only)
POST
http://localhost:8080/jobs

Header:

Authorization: Bearer <ADMIN_TOKEN>
{
  "title": "Spring Boot Developer",
  "description": "Backend + Kafka",
  "company": "TechCorp",
  "location": "Pune"
}
5. View Jobs (Public)
GET
http://localhost:8080/jobs?page=0&size=5&sortBy=createdAt&direction=desc

6. Apply to Job (USER Only)
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

7. Update Application Status (ADMIN Only)
PATCH
http://localhost:8080/applications/1/status

Header:

Authorization: Bearer <ADMIN_TOKEN>
{
  "status": "ACCEPTED"
}
Kafka event published.
Application status updated in database.

Notification Microservice
Runs separately at:

http://localhost:8081
Consumes Kafka events and stores notifications in:

notification_db
8. Check Notifications
GET
http://localhost:8081/notifications

Example response:

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
9. Mark Notification as Read
PATCH
http://localhost:8081/notifications/3/read

After that:

GET
http://localhost:8081/notifications

Now:

"read": true
Design Decisions
DTO isolation (no entity exposure)

Sort field whitelist

Global exception handling

Role-based access control

Unique application prevention

Stateless security

Open-In-View disabled

Event-driven decoupling

Microservice Design
The monolith publishes domain events.
The notification service consumes events independently.

Benefits:

Service decoupling

Horizontal scalability

Independent deployment

Clean domain separation

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

Runs on port 8081