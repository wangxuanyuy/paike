# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a multi-module school course management system ("排课系统") with separate student and teacher portals. The system manages course scheduling, classroom allocation, time slots, and student enrollment.

**Architecture**: Multiple Spring Boot backends + Vue 3 frontends, with a shared MySQL database.

## Project Structure

```
paike/
├── student-course-backend/          # Student system backend (Port: 45081)
├── student-course-frontend/         # Student system frontend (Port: 3000)
├── teacher-f2/                      # Teacher backend (alternative)
├── teacher-f1/                      # Teacher frontend (alternative)
├── teacher-scheduling-system/       # Teacher scheduling system (Port: 45082 backend, 45100 frontend)
├── classroom-timeslot-management/   # Classroom & timeslot admin UI (Port: 45103)
└── database-schema.sql              # Shared database schema
```

**Note**: There are multiple teacher/student modules at different stages of development. The main active modules are:
- `student-course-backend` + `student-course-frontend` for student portal
- `classroom-timeslot-management` for admin management
- `teacher-scheduling-system` for teacher scheduling

## System Requirements

- **Java**: 17+
- **Node.js**: 16+
- **Maven**: 3.6+
- **MySQL**: 8.0+ (Port: 45306 or 3306)
- **Redis**: 6.0+ (optional for caching)

## Quick Start Commands

### Database Setup
```bash
# Create database
mysql -u root -p -e "CREATE DATABASE school_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Import schema
mysql -u root -p school_system < database-schema.sql

# Import initial data (if exists)
mysql -u root -p school_system < student-course-backend/src/main/resources/data.sql
```

### Student Course System (Main System)

**Backend** (Port 45081):
```bash
cd student-course-backend
mvn clean install
mvn spring-boot:run
```

**Frontend** (Port 3000):
```bash
cd student-course-frontend
npm install
npm run dev
```

**Build for Production**:
```bash
# Backend
cd student-course-backend
mvn clean package
java -jar target/student-course-backend-1.0.0.jar

# Frontend
cd student-course-frontend
npm run build
# Deploy dist/ folder to web server
```

### Classroom & Timeslot Management (Port 45103)

```bash
cd classroom-timeslot-management
npm install
npm run dev

# Build for production
npm run build
```

**Note**: This module connects to KrakenD API Gateway on port 45180, which proxies to backend services.

### Teacher Scheduling System

**Backend** (Port 45082):
```bash
cd teacher-scheduling-system/backend
mvn spring-boot:run

# Or use the script
./start-backend.sh
```

**Frontend** (Port 45100):
```bash
cd teacher-scheduling-system/frontend
npm install
npm run dev

# Or use the script
./start-frontend.sh
```

## Architecture & Technical Stack

### Backend (Spring Boot 3.2.0)
- **Framework**: Spring Boot, Spring Data JPA
- **Database**: MySQL 8.0 with JPA/Hibernate
- **Caching**: Redis (optional)
- **Validation**: Bean Validation
- **API**: RESTful architecture

**Package Structure**:
```
com.school.course/
├── config/          # Configuration (CORS, JPA, Redis)
├── controller/      # REST API endpoints
├── dto/            # Data Transfer Objects
├── entity/         # JPA entities (map to database tables)
├── exception/      # Exception handling
├── repository/     # JPA repositories
└── service/        # Business logic
```

**Key Configuration**:
- Default port: 45081 (student backend), 45082 (teacher backend)
- Database naming strategy: Physical names preserved (no snake_case conversion)
- JPA DDL: `ddl-auto: none` - database schema managed via SQL scripts
- Active profile: `dev` (see `application-dev.yml` for database credentials)

### Frontend (Vue 3 + TypeScript)
- **Framework**: Vue 3 with Composition API
- **Language**: TypeScript
- **UI Library**: Element Plus
- **Build Tool**: Vite
- **State Management**: Pinia
- **HTTP Client**: Axios
- **Auto-imports**: unplugin-auto-import, unplugin-vue-components

**Directory Structure**:
```
src/
├── api/            # API request functions
├── components/     # Reusable components
├── router/         # Vue Router configuration
├── stores/         # Pinia stores
├── types/          # TypeScript type definitions
├── utils/          # Utility functions (request.ts for Axios)
└── views/          # Page components
```

**API Proxy Configuration**:
- Student frontend (port 3000) → Backend (port 45081)
- Classroom management (port 45103) → KrakenD Gateway (port 45180) → Backend services

## Database Schema

**Core Tables**:
- `T_TIME_SLOTS` - Time slots for scheduling (with day_of_week 1-7)
- `J_CLASSROOMS` - Classroom information
- `K_COURSES` - Course definitions (with age_group 1-4)
- `T_COURSE_SCHEDULE` - Links courses to time slots and classrooms
- `X_STUDENTS` - Student information
- `T_STUDENT_ENROLLMENT` - Student course enrollments

**Naming Conventions**:
- Table prefixes: T_ (time/schedule), J_ (classroom), K_ (course), X_ (student)
- Columns: snake_case (e.g., `course_code`, `max_capacity`)
- Boolean fields: `is_active`, `is_approved`
- Timestamps: `created_at`, `updated_at`

**Important**: JPA entities use direct field mapping (e.g., `@Column(name = "course_code")`) to match exact database column names.

## Common Development Tasks

### Adding a New Entity
1. Create table in `database-schema.sql`
2. Create JPA entity in `entity/` package with exact column name mappings
3. Create repository interface extending `JpaRepository`
4. Create service class for business logic
5. Create DTO for API responses
6. Create controller with REST endpoints

### Adding a New Frontend Page
1. Create Vue component in `src/views/`
2. Add route in `src/router/index.ts`
3. Create API functions in `src/api/`
4. Define TypeScript types in `src/types/`
5. Use Element Plus components for UI

### Database Configuration
Edit `student-course-backend/src/main/resources/application-dev.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/school_system
    username: root
    password: root
```

### CORS Configuration
CORS is configured in backend `config/CorsConfig.java`. Allowed origins include localhost ports used by frontends.

## Port Reference

| Service | Port | Path |
|---------|------|------|
| Student Backend | 45081 | student-course-backend |
| Student Frontend | 3000 | student-course-frontend |
| Teacher Backend | 45082 | teacher-scheduling-system/backend |
| Teacher Frontend | 45100 | teacher-scheduling-system/frontend |
| Classroom Management | 45103 | classroom-timeslot-management |
| KrakenD Gateway | 45180 | (external service) |
| MySQL Database | 45306 or 3306 | - |

## API Endpoints

### Student System (Port 45081)

**Students**:
- `GET /api/students` - Get all students
- `POST /api/students/register` - Register new student
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student

**Courses**:
- `GET /api/courses` - Get all courses
- `GET /api/courses/schedule` - Get course schedule view
- `POST /api/courses` - Create course
- `PUT /api/courses/{id}` - Update course
- `DELETE /api/courses/{id}` - Delete course

**Enrollments**:
- `GET /api/enrollments/student/{studentId}` - Get student enrollments
- `POST /api/enrollments` - Enroll student in course
- `DELETE /api/enrollments` - Remove enrollment

**Time Slots**:
- `GET /api/time-slots` - Get all time slots (paginated)
- `GET /api/time-slots/all` - Get all time slots
- `POST /api/time-slots` - Create time slot
- `POST /api/time-slots/batch-create` - Batch create time slots for multiple days
- `PUT /api/time-slots/{id}` - Update time slot
- `DELETE /api/time-slots/{id}` - Delete time slot

**Classrooms**:
- `GET /api/classrooms` - Get classrooms (paginated)
- `GET /api/classrooms/all` - Get all classrooms
- `POST /api/classrooms` - Create classroom
- `PUT /api/classrooms/{id}` - Update classroom
- `DELETE /api/classrooms/{id}` - Delete classroom
- `GET /api/classrooms/usage` - Get classroom usage statistics

## Development Notes

### JPA Entity Mapping
The system uses `PhysicalNamingStrategyStandardImpl` to preserve exact column names. Always specify `@Column(name = "exact_db_name")` in entities.

### Age Groups
Courses are categorized into 4 age groups (1-4). Students should only enroll in courses matching their age group.

### Day of Week
Time slots use `day_of_week` field: 1 = Monday, 2 = Tuesday, ..., 7 = Sunday

### Conflict Detection
The system should prevent scheduling conflicts:
- Same classroom cannot be used by multiple courses at the same time slot
- Teacher assignments should avoid time conflicts

### Redis Caching
Redis is configured but optional. The system works with in-memory caching if Redis is unavailable.

## Testing

**Backend Tests**:
```bash
cd student-course-backend
mvn test
```

**Frontend**:
No test framework currently configured. Manual testing recommended.

## Troubleshooting

### Port Already in Use
```bash
# Windows
netstat -ano | findstr :<PORT>
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:<PORT> | xargs kill
```

### Database Connection Issues
1. Verify MySQL is running on configured port (45306 or 3306)
2. Check credentials in `application-dev.yml`
3. Ensure database `school_system` exists
4. Verify JDBC connection string format

### Frontend Cannot Connect to Backend
1. Check backend is running and accessible
2. Verify proxy configuration in `vite.config.ts`
3. Check CORS settings in backend
4. Inspect browser console for errors

### Maven Build Fails
1. Ensure Java 17+ is installed: `java -version`
2. Clear Maven cache: `mvn clean`
3. Check `pom.xml` dependencies

### NPM Install Fails
```bash
npm cache clean --force
rm -rf node_modules package-lock.json
npm install
```
