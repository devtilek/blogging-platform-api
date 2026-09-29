# Blogging Platform API

A backend REST API for a blogging platform built with Java 17 and Spring Boot 4. The project demonstrates REST API design, PostgreSQL persistence, Flyway migrations, JWT authentication, ownership-based authorization, validation, pagination, Docker and automated tests.

## Features

- CRUD operations for posts
- Search posts by title, content or category
- Pagination and sorting
- Maximum page size protection
- JWT registration and login
- BCrypt password hashing
- Role model: USER and ADMIN
- Post ownership checks
- Public read access for posts
- Authenticated create/update/delete operations
- Request validation
- Structured validation and error responses
- MapStruct DTO mapping
- PostgreSQL persistence
- Flyway database migrations
- H2 database for tests
- Transactional service layer
- Docker Compose local environment
- GitHub Actions CI

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1.1 |
| Web | Spring MVC |
| Security | Spring Security 7, JWT |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Migrations | Flyway |
| Mapping | MapStruct |
| Validation | Jakarta Bean Validation |
| Build | Gradle |
| Testing | JUnit 5, Mockito, H2 |
| Containerization | Docker, Docker Compose |
| CI | GitHub Actions |

## Architecture

```text
HTTP Request
     ↓
Controller
     ↓
Service
     ↓
Repository
     ↓
PostgreSQL

Security:
Request → JWT Filter → Spring Security → Protected endpoint

Mapping:
Request DTO ↔ MapStruct ↔ Entity ↔ MapStruct ↔ Response DTO
```

Business rules such as post ownership are kept in the service layer rather than relying only on controller-level checks.

## Authentication

### Register

```http
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

New accounts are created with the `USER` role.

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

The response contains a JWT access token:

```json
{
  "accessToken": "eyJ...",
  "tokenType": "Bearer",
  "email": "user@example.com",
  "role": "USER"
}
```

Use the token for protected requests:

```http
Authorization: Bearer <access-token>
```

## API

### Posts

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/posts` | Public | List posts |
| GET | `/posts/{id}` | Public | Get a post |
| POST | `/posts` | Authenticated | Create a post |
| PUT | `/posts/{id}` | Owner/Admin | Update a post |
| DELETE | `/posts/{id}` | Owner/Admin | Delete a post |

### Create post

```http
POST /posts
Authorization: Bearer <access-token>
Content-Type: application/json

{
  "title": "My first post",
  "content": "Hello from the blogging platform.",
  "category": "Java",
  "tags": ["java", "spring"]
}
```

The author is taken from the authenticated user. Clients cannot assign another user as the author.

### List posts

```http
GET /posts?page=0&size=10
```

Posts are returned newest first by default. The API limits requested page size to 50.

### Search

```http
GET /posts?term=spring&page=0&size=10
```

Search checks the title, content and category.

### Update

Only the post author or an ADMIN can update a post.

### Delete

Only the post author or an ADMIN can delete a post. Successful deletion returns:

```http
204 No Content
```

## Error Response

Validation errors use a consistent structure:

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2026-09-29T12:00:00",
  "errors": {
    "title": "must not be blank"
  }
}
```

The API also uses appropriate HTTP status codes such as 401 for invalid credentials, 403 for forbidden operations, 404 for missing posts and 409 for conflicts.

## Database

Flyway manages schema changes:

```text
V1__create_posts_schema.sql
V2__add_users_and_post_authors.sql
```

Hibernate uses `ddl-auto=validate`, so the application validates the database schema instead of modifying it automatically.

Posts created before the author migration may have a null author and are therefore not editable or deletable by regular users.

## Configuration

Required environment variables:

```text
DB_URL=jdbc:postgresql://localhost:5432/blogging_platform
DB_USERNAME=postgres
DB_PASSWORD=your_password

JWT_SECRET_BASE64=<base64-encoded-secret>
```

The JWT secret should contain at least 256 bits of random key material. Never commit a real secret to Git.

## Run with Gradle

Create the PostgreSQL database, configure the environment variables and run:

```bash
./gradlew bootRun
```

Windows:

```powershell
.\gradlew.bat bootRun
```

Run tests:

```bash
./gradlew test
```

## Run with Docker Compose

Set `JWT_SECRET_BASE64` in your environment, then:

```bash
docker compose up --build
```

The API starts on port 8080 and PostgreSQL on port 5432.

Stop the environment:

```bash
docker compose down
```

Remove the database volume as well:

```bash
docker compose down -v
```

## Project Structure

```text
src/main/java/com/bloggingplatformapi
├── config
├── controller
├── dto
├── entity
├── exception
├── mapper
├── repository
├── security
└── service
    └── impl

src/main/resources
├── db/migration
└── application.properties

src/test/java
└── com/bloggingplatformapi
```

## Development Principles

- Business logic belongs in services.
- Controllers handle HTTP concerns.
- DTOs keep the API contract separate from JPA entities.
- Database schema changes are versioned with Flyway.
- Secrets are provided through environment variables.
- Passwords are stored using BCrypt hashes.
- JWT authentication is stateless.
- Ownership checks prevent users from modifying other users' posts.
- Automated tests protect service and controller behavior.

## Roadmap

- OpenAPI / Swagger documentation
- Integration tests with Testcontainers
- Refresh tokens
- Comments and likes
- Advanced search
- Redis caching
- Rate limiting
