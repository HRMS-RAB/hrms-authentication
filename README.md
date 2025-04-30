# HRMS Authentication Microservice

## Prerequisites
* Java 17
* Maven 3.9+
* Docker & Docker Compose (optional but recommended)
* PostgreSQL (if not using Docker)
* RabbitMQ (if not using Docker)
* Eclipse IDE (or any preferred IDE)

## Setup Steps

1. **Clone/Copy** the `hrms-authentication` folder into `C:\Users\Lenovo\HRMS RAB\`.
2. **Import** the Maven project into Eclipse:  
   *File → Import → Existing Maven Projects → Browse to `hrms-authentication` → Finish*.
3. **Configure** environment variables if not using defaults (`application.properties`).
4. **Run** Flyway migrations automatically at startup or manually via Maven:  
   `mvn -Dflyway.configFiles=src/main/resources/application.properties flyway:migrate`.
5. **Start** RabbitMQ and PostgreSQL:  
   *Option A* – `docker compose up -d`  
   *Option B* – Start services manually if installed locally.
6. **Build** and run the service:  
   `mvn clean package`  
   `java -jar target/hrms-authentication-1.0.0.jar`
7. **Test** login via curl:  

```bash
curl -X POST http://localhost:8082/auth/login   -H "Content-Type: application/json"   -d '{"username":"john.doe@corporate.com","password":"Password@1"}'
```

8. **Secure Endpoints**: Copy the returned JWT token and add it to *Authorization: Bearer <token>* header for subsequent requests.

## Useful Maven Commands
* `mvn spring-boot:run` – Hot reload during development  
* `mvn test` – Run unit tests

## Notes
* Default password for auto‑created users is `Password@1`. In production, enforce immediate password change on first login.
* Customise role logic in `RoleAssignmentServiceImpl`.
* Update `hrms.jwt.secret` before deploying to production.
