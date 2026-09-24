# Blogging Platform API

REST API for a blogging platform built with Java and Spring Boot.

## Features

- Create, read, update and delete posts
- Search posts by title, content or category
- Pagination and sorting
- Request validation
- Centralized error handling
- MapStruct DTO mapping
- PostgreSQL for production
- H2 for isolated tests
- Transactional service layer
- GitHub Actions CI

## Tech Stack

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA / Hibernate
- PostgreSQL
- H2
- MapStruct
- Lombok
- Gradle
- JUnit

## Architecture

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

DTOs are mapped to entities and responses through MapStruct.

## API

### Create post

```http
POST /posts
Content-Type: application/json

{
  "title": "My first post",
  "content": "Hello from the blogging platform.",
  "category": "Java",
  "tags": ["java", "spring"]
}
```

### Get posts

```http
GET /posts?page=0&size=10
```

Posts are returned newest first by default.

### Search posts

```http
GET /posts?term=spring&page=0&size=10
```

Search checks the title, content and category.

### Get post

```http
GET /posts/{id}
```

### Update post

```http
PUT /posts/{id}
Content-Type: application/json
```

### Delete post

```http
DELETE /posts/{id}
```

Returns `204 No Content` when the post is deleted.

## Configuration

Database credentials are provided through environment variables:

```text
DB_URL=jdbc:postgresql://localhost:5432/blogging_platform
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

For tests, the project automatically uses an in-memory H2 database.

## Run locally

Set the database environment variables and run:

```bash
./gradlew bootRun
```

Run tests:

```bash
./gradlew test
```

## Project Structure

```text
src/main/java/com/bloggingplatformapi
├── controller
├── dto
├── entity
├── exception
├── mapper
├── repository
└── service
    └── impl
```

## Next Improvements

- OpenAPI / Swagger documentation
- Flyway database migrations
- Authentication and authorization
- User accounts and post ownership
- Comments and likes
- Integration tests
- Docker Compose
