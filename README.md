# Task and Team Management REST API

## Build and Run Commands

### To build the project:
```bash
./gradlew build
```

### To run the application:
```bash
./gradlew bootRun
```

### Alternative commands:
- **Clean and build:** `./gradlew clean build`
- **Run tests:** `./gradlew test`
- **Build without tests:** `./gradlew build -x test`

## Database Tables

The application uses the following database tables:

- **users** - User information and authentication
- **projects** - Project details and management
- **task** - Task tracking and assignments
- **report** - Reporting and analytics data

## Prerequisites

- Java 21
- MySQL Database
- Gradle Wrapper (included)