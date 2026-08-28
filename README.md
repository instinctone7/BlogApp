BlogApp

A RESTful blog backend built with Spring Boot, featuring JWT-based authentication with email verification, and full CRUD for posts, categories, and tags.

Status: Work in progress. See Known Issues / TODO before treating this as production-ready.

Tech Stack
Java 25
Spring Boot 4.1.1
Spring Security — stateless JWT authentication
Spring Data JPA (Hibernate) — persistence
MySQL — database
MapStruct 1.6.3 — entity ↔ DTO mapping
Lombok — boilerplate reduction
jjwt 0.13.0 — JWT generation/validation
Spring Boot Actuator — health/metrics endpoints
Jakarta Bean Validation — request validation
Features
User registration with email verification (token-based, 8-hour expiry)
JWT sign-in
Blog post CRUD, with category and tag associations
Category and tag management
Global stateless auth via a custom JWT filter (JwtFilter)
Centralized error handling (GlobalExceptionHandler) — consistent JSON error shape (timestamp, status, error, message, path) for not-found, validation, malformed-body, and auth/authorization failures
Project Structure
src/main/java/com/InstinctOne/BlogApp/
├── config/          # Spring Security filter chain
├── controllers/      # REST controllers (auth, posts, categories, tags)
├── dtos/              # Request/response DTOs
├── entities/          # JPA entities
├── enums/             # PostStatus
├── exceptions/        # Custom exceptions + GlobalExceptionHandler
├── mappers/           # MapStruct entity-DTO mappers
├── repositories/      # Spring Data JPA repositories
├── security/           # JwtFilter, JwtUtil
└── services/            # Business logic
Getting Started
Prerequisites
JDK 25
MySQL running locally (or update application.properties to point elsewhere)
Maven (or use the included ./mvnw wrapper)
Setup
Clone the repo and navigate into the project:
bash
   git clone <repo-url>
   cd BlogApp
Configure your database in src/main/resources/application.properties:
properties
   spring.datasource.url=jdbc:mysql://localhost:3306/blogdb?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=<your-password>

The DB schema is auto-managed via spring.jpa.hibernate.ddl-auto=update — fine for local development, not recommended for production (see Known Issues).

Build and run:
bash
   ./mvnw spring-boot:run

The app starts on http://localhost:8080.

API Reference
Auth — /api/auth (public)
Method	Endpoint	Description
POST	/register	Register a new user; sends back a verification link
GET	/verification?token=	Verify email via token from the registration link
POST	/signIn	Log in with email + password; returns a JWT

All other endpoints require Authorization: Bearer <token>.

Posts — /api/posts
Method	Endpoint	Description
POST	/create	Create a post (category by name, tags by name list)
GET	``	List all posts
GET	/{id}	Get a single post
PUT	/update/{id}	Update a post
DELETE	/delete/{id}	Delete a post
Categories — /api/categories
Method	Endpoint	Description
POST	/create	Create a category
DELETE	/remove	Delete a category
Tags — /api/tags
Method	Endpoint	Description
POST	/create	Create a tag
DELETE	/remove	Delete a tag
Error Response Shape

All errors return a consistent JSON body:

json
{
  "timestamp": "2026-08-29T10:15:30",
  "status": 404,
  "error": "Not Found",
  "message": "Post with id 42 not found",
  "path": "/api/posts/42"
}
