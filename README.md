# ResolveIT — Online Complaint Management System

A starter college project built with Java 17, Spring Boot, Thymeleaf, HTML and CSS.

## Current features
- Campus complaint dashboard and sample statistics
- Submit a complaint with a generated numeric tracking ID (`RI-1001`, etc.)
- Track a complaint by entering the numeric part of its ID
- Admin console to search/filter complaints and update status/remarks
- Responsive web layout for desktop and mobile browser

## Requirements
- JDK 17
- IntelliJ IDEA
- Internet access the first time Maven downloads dependencies

## Run in IntelliJ
1. Extract the ZIP.
2. In IntelliJ, choose **File → Open** and select the extracted `ResolveIT` folder (the folder containing `pom.xml`).
3. Trust the project if asked.
4. Allow Maven to import dependencies.
5. Open `src/main/java/com/resolveit/ResolveItApplication.java`.
6. Click the green Run triangle beside `main`.
7. Open `http://localhost:8080`.

Alternatively, from a terminal in the project folder run:
- Windows: `mvnw.cmd spring-boot:run`
- macOS/Linux: `./mvnw spring-boot:run`

If the Maven wrapper script is not included in the extracted folder, use the Maven tool window in IntelliJ or install Maven.

## Important limitations of this starter
This version is a **demo prototype**, not production-ready:
- Complaints are held in memory and reset when the app restarts.
- Sample complaints are inserted on startup.
- There is no real user authentication or admin authorization yet.
- File uploads, email notifications, and a persistent MySQL database are not implemented yet.
- The Android app is not included in this first ZIP; it should consume REST APIs from a later backend iteration.
- Do not use real sensitive personal data in this prototype.

## Recommended next implementation steps
1. Add MySQL persistence with Spring Data JPA.
2. Add secure student/admin authentication and role-based authorization.
3. Build REST endpoints for Android.
4. Add file upload validation and notifications.
5. Create Android Studio app screens that call the backend API.


## Role-based login (demo implementation)

The application now includes Spring Security form login with two roles:

- **USER** opens the Student Workspace at `/user`.
- **ADMIN** opens the Administrator Complaint Console at `/admin`.

Demo credentials for local testing:

- Student: `student` / `Student@123`
- Administrator: `admin` / `Admin@123`

The passwords are BCrypt-encoded by Spring Security when the application starts. You can override the demo usernames and passwords with these environment variables:

- `DEMO_STUDENT_USERNAME`
- `DEMO_STUDENT_PASSWORD`
- `DEMO_ADMIN_USERNAME`
- `DEMO_ADMIN_PASSWORD`

**Before redeploying publicly:** set all four variables in Render's Environment settings, especially a unique strong administrator password. Do not use the demo credentials for real users.

### Current prototype limitations

Accounts are currently configured in memory from environment variables; there is no self-service registration or per-student account database yet. Complaint records are also still stored in memory and can reset when the service restarts. The student workspace currently provides shared campus summary counts and complaint actions; individual complaint ownership/privacy requires the next database-and-account phase. Use fictional test information until persistent storage, individual accounts, and ownership checks are implemented.

### Run locally

Use Java 17 or later and run:

```bash
mvn spring-boot:run
```

Open `http://localhost:8080` and choose **Sign in**.
