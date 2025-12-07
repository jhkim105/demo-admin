# Demo Admin Backend

Spring Boot + Kotlin backend that provides JWT authentication and user/role management APIs for the admin app.

## Prerequisites
- JDK 21
- Gradle (wrapper included via `./gradlew`)

## Running
```bash
cd backend
./gradlew bootRun
```

The server starts on `http://localhost:8080`. Swagger UI is available at `http://localhost:8080/swagger-ui/index.html`.

## Default credentials
- Email: `admin@example.com`
- Password: `admin123`

## API docs (Spring REST Docs)
- Generate: `./gradlew test asciidoctor copyDocs`
- View locally: open `docs/index.html` (generated in project root) or run the app and visit `http://localhost:8080/docs/index.html` (packaged into the jar).

## Key endpoints
- `POST /api/auth/login` — login with email/password, returns JWT
- `GET /api/users` — list users (paging with `page`, `size`)
- `GET /api/users/{id}` — get user detail
- `POST /api/users` — create user
- `PUT /api/users/{id}` — update user
- `DELETE /api/users/{id}` — delete user
- `GET /api/roles` — list roles

JWT settings are in `src/main/resources/application.yml` (`app.jwt.secret`, `expiration-minutes`).
