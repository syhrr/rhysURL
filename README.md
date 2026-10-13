# rhysURL

A URL shortener REST API built with **Java 21**, **Spring Boot** and **PostgreSQL**. Post a long URL, get back a short code, and visit `/{code}` to be redirected. Links can optionally expire, and every redirect is counted.

Built as a learning project to practise REST API design, persistence, validation, error handling and containerisation.

## Features

- Create short links from any valid URL (`POST /api/urls`)
- Fast `302` redirects (`GET /{code}`)
- Optional expiry time per link, with `410 Gone` once expired
- Hit counter and last-accessed timestamp on every redirect
- Random 7-character Base62 codes from `SecureRandom`, with retry if a code is already taken
- Input validation, with consistent [RFC 7807](https://www.rfc-editor.org/rfc/rfc7807) `application/problem+json` error responses
- One-command Docker setup (app + PostgreSQL), or run locally against in-memory H2

## Tech stack

| Area        | Choice                                  |
|-------------|-----------------------------------------|
| Language    | Java 21                                 |
| Framework   | Spring Boot (Web MVC, Data JPA, Validation) |
| Database    | PostgreSQL 16 (H2 for local dev)        |
| Build       | Maven (wrapper included)                |
| Packaging   | Docker, Docker Compose                  |

## Quick start

**With Docker (app + PostgreSQL):**
```bash
docker compose up --build
```
The API is then available at http://localhost:8080. Stop it with `docker compose down` (add `-v` to wipe the database).

**Locally (in-memory H2, no Docker needed):**
```bash
./mvnw spring-boot:run
```
The H2 console is enabled in this mode.

## API

### Create a short URL

`POST /api/urls`

| Field       | Type              | Required | Notes                                  |
|-------------|-------------------|----------|----------------------------------------|
| `targetUrl` | string            | yes      | Must be a valid URL                    |
| `expiresAt` | ISO-8601 instant  | no       | e.g. `2030-01-01T00:00:00Z`            |

```bash
curl -i -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{"targetUrl": "https://www.example.com/some/very/long/path"}'
```

Response: `201 Created`, with the short path in the `Location` header.
```json
{
  "code": "Ab3dE9x",
  "targetUrl": "https://www.example.com/some/very/long/path",
  "expiresAt": null,
  "createdAt": "2026-10-13T12:00:00Z"
}
```

### Follow a short URL

`GET /{code}`

```bash
curl -i http://localhost:8080/Ab3dE9x
# HTTP/1.1 302
# Location: https://www.example.com/some/very/long/path
```

### Errors

All errors are returned as `application/problem+json`.

| Status | When                                   |
|--------|----------------------------------------|
| `400`  | Request body fails validation (the response lists each invalid field) |
| `404`  | The short code does not exist          |
| `410`  | The short link has expired             |
| `500`  | Unexpected server error                |

Example:
```bash
curl -i -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{"targetUrl": "not a url"}'
```
```json
{
  "title": "Invalid request",
  "status": 400,
  "detail": "Validation failed",
  "errors": { "targetUrl": "must be a valid URL" }
}
```

## How it works

### Shortening
1. The request is validated (`@NotBlank`, `@URL`).
2. `CodeGenerator` produces a random 7-character code from the 62-character alphabet `0-9A-Za-z`, giving about 3.5 trillion possible codes.
3. `ShortUrlService` checks the code isn't already used. If it is, it generates another, up to 5 attempts, before failing.
4. The mapping (code, target URL, expiry, hit count) is saved through Spring Data JPA.

### Redirection
1. The code is looked up in the database; an unknown code gives `404`.
2. If the link has an expiry in the past, the response is `410 Gone`.
3. Otherwise the hit counter and last-accessed time are updated and the client gets a `302` to the original URL.

A `302` is used rather than a `301` so browsers don't cache the redirect, which would stop later visits from being counted.

## Project structure

```
src/main/java/com/example/urlshortener/
├── controller/   REST endpoints and request DTOs
├── service/      business logic, code generation, global exception handler
├── repository/   Spring Data JPA repository
├── model/        ShortUrl entity and response DTO
└── exception/    domain exceptions mapped to HTTP statuses
```

## Configuration

| Profile              | Database          | Schema handling |
|----------------------|-------------------|-----------------|
| default              | in-memory H2 (PostgreSQL mode) | `update` |
| `prod` (Docker)      | PostgreSQL        | `validate` (Compose overrides to `update`) |

The `prod` profile reads these environment variables: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`.

## Tests

```bash
./mvnw test
```

## Roadmap

- [ ] Return the existing short link when the same target URL is submitted again (or `409 Conflict`)
- [ ] Flyway migrations instead of `ddl-auto: update`
- [ ] Integration tests with Testcontainers
- [ ] OpenAPI / Swagger UI
- [ ] Rate limiting and Redis caching for redirects
- [ ] GitHub Actions CI
