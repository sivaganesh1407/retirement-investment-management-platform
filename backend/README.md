# Retirement Investment Management Platform – Backend

Spring Boot backend for the Retirement Investment Management Platform. Uses Java 17, Spring Security with JWT, Spring Data JPA, H2 for development, and is ready to run in the cloud (Docker + cloud profile).

## Package layout

| Package     | Purpose |
|------------|---------|
| `controller` | REST endpoints; request validation and response handling |
| `service`    | Business logic and transaction boundaries |
| `repository` | Spring Data JPA data access |
| `model`      | JPA entities and DTOs |
| `security`   | JWT generation/validation, user details, auth filter |
| `config`     | Security config, exception handling, beans |

## Requirements

- **Java 17**
- Maven 3.6+

## Configuration

- **JWT_SECRET** (required): Environment variable for signing JWTs. Must be at least 32 bytes (e.g. `openssl rand -hex 32`). Never commit a real secret to version control.

  Local run example:
  ```bash
  export JWT_SECRET=$(openssl rand -hex 32)
  mvn spring-boot:run
  ```

## Run locally

```bash
mvn spring-boot:run
```

Server: `http://localhost:8080`

## Run with Docker (backend + cloud profile)

From the `backend` directory:

```bash
# Build image
docker build -t retirement-platform-backend .

# Run (set JWT_SECRET; PORT is optional, default 8080)
docker run -p 8080:8080 -e JWT_SECRET=$(openssl rand -hex 32) retirement-platform-backend
```

For a cloud database (e.g. PostgreSQL), pass the datasource env vars:

```bash
docker run -p 8080:8080 \
  -e JWT_SECRET=your-secret-min-32-chars \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/dbname \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=pass \
  retirement-platform-backend
```

## Deploy to the cloud

The app uses the **cloud** profile when run in Docker (`SPRING_PROFILES_ACTIVE=cloud`). For cloud platforms:

1. **Build** the Docker image (or use a buildpack / `mvn package`).
2. **Set environment variables** in the platform:
   - `JWT_SECRET` (required) – at least 32 bytes.
   - `PORT` – often set by the platform (Heroku, Cloud Run, App Service, etc.).
   - For a managed DB: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`.
3. **Optional:** `SPRING_JPA_HIBERNATE_DDL=update` (default in cloud profile).

Examples:

- **Google Cloud Run:** Push image to Artifact Registry or Container Registry, deploy with Cloud Run and set the env vars above.
- **AWS (ECS / App Runner):** Deploy the image to ECR, run as a service with the same env vars.
- **Azure App Service:** Deploy as a Linux container and configure Application Settings for the env vars.
- **Heroku:** Use Heroku container registry or buildpack; set Config Vars for `JWT_SECRET` and `DATABASE_URL` (or `SPRING_DATASOURCE_*`).

## H2 console

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:financial_platform`
- User: `sa`, Password: (empty)

## API overview

### Auth (no JWT required)

- `POST /auth/register` – Register (body: name, email, password, optional role). Returns JWT.
- `POST /auth/login` – Login (body: email, password). Returns JWT.

### Customers (JWT required)

- `POST /customers` – Create customer (body: name, email, retirementGoal, riskProfile).
- `GET /customers` – List all customers.
- `GET /customers/{id}` – Get customer by ID.

### Portfolio (JWT required)

- `POST /portfolio` – Create portfolio (body: customerId, portfolioName).
- `GET /portfolio/{customerId}` – List portfolios for customer.

### Retirement (JWT required)

- `GET /retirement/projection/{customerId}` – Get projection (currentSavings, projectedValue, retirementAge).

## Authentication

Send the JWT in the `Authorization` header:

```
Authorization: Bearer <token>
```

Obtain the token from `POST /auth/register` or `POST /auth/login`.
