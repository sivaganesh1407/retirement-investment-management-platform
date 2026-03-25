# Retirement Investment Management Platform – Backend

Spring Boot backend for the Retirement Investment Management Platform. Uses Java 11, Spring Security with JWT, Spring Data JPA, H2 for development, and is ready to run in the cloud (Docker + cloud profile).

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

- **Java 11** or later (project is built and tested with Java 11)
- Maven 3.6+

**Check your Java version:** `java -version` and `mvn -v`. The project compiles with Java 11 (e.g. Amazon Corretto 11, Temurin 11, or OpenJDK 11).

## Configuration

- **JWT_SECRET** (required): Environment variable for signing JWTs. Must be at least 32 bytes (e.g. `openssl rand -hex 32`). Never commit a real secret to version control.

  **Easiest:** use the run script (sets JWT_SECRET automatically if missing):
  ```bash
  ./run.sh
  ```
  Or set it yourself and use Maven:
  ```bash
  export JWT_SECRET=$(openssl rand -hex 32)
  ./mvnw spring-boot:run
  ```

## Run locally

**Option 1 – run script (recommended):** sets `JWT_SECRET` if not set, then starts the app.

From the **project root** (retirement-investment-management-platform):
```bash
cd backend
./run.sh
```
If you're **already in the `backend` folder**, just run `./run.sh` (do not run `cd backend` again).

**Option 2 – Maven directly:**

```bash
cd backend
export JWT_SECRET=$(openssl rand -hex 32)
./mvnw spring-boot:run
# or: mvn spring-boot:run
```

Server: `http://localhost:8081`

- **`.java-version`** is set to `11` so tools (SDKMAN, asdf, IDEs) can pick the right JDK.
- **`./mvnw`** uses system Maven if present. For a full wrapper that downloads Maven, run once: `mvn -N wrapper:wrapper`.

## Run with Docker (backend + cloud profile)

From the `backend` directory.

**Option 1 – Docker Compose (recommended):**

```bash
# Set JWT_SECRET (required)
export JWT_SECRET=$(openssl rand -hex 32)
docker compose up -d
```

Or copy `.env.example` to `.env`, set `JWT_SECRET` there, then run `docker compose up -d`. The API will be at `http://localhost:8081`. Stop with `docker compose down`.

**Option 2 – Build and run with docker only:**

```bash
# Build image
docker build -t retirement-platform-backend .

# Run (set JWT_SECRET; PORT is optional, default 8081)
docker run -p 8081:8081 -e JWT_SECRET=$(openssl rand -hex 32) retirement-platform-backend
```

**With a cloud database (e.g. PostgreSQL):** pass the datasource env vars in `docker run` or in your `docker-compose.yml` / `.env`:

```bash
docker run -p 8081:8081 \
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

## Health check

For monitoring and cloud readiness, the app exposes:

- **`GET /actuator/health`** – returns `{"status":"UP"}` (no auth). Use for load balancers and container health checks.
- **`GET /actuator/info`** – app info (no auth).

## H2 console

- URL: `http://localhost:8081/h2-console`
- JDBC URL: `jdbc:h2:mem:financial_platform`
- User: `sa`, Password: (empty)

## API documentation (Swagger)

- **Swagger UI:** `http://localhost:8081/swagger-ui.html` (no auth to view; use "Authorize" with a JWT to try protected endpoints).
- **OpenAPI JSON:** `http://localhost:8081/v3/api-docs`

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
- `GET /portfolio/customer/{customerId}` – List portfolios for customer.
- `GET /portfolio/{portfolioId}/investments` – List investments in a portfolio.
- `POST /portfolio/{portfolioId}/investments` – Add investment (body: assetName, assetType, amount, purchaseDate).

### Retirement (JWT required)

- `GET /retirement/projection/{customerId}` – Get projection (currentSavings, projectedValue, retirementAge).

## Authentication

Send the JWT in the `Authorization` header:

```
Authorization: Bearer <token>
```

Obtain the token from `POST /auth/register` or `POST /auth/login`.

---

## How to test

### 1. Start the backend

```bash
cd backend
./run.sh
```

Or with explicit JWT_SECRET: `export JWT_SECRET=$(openssl rand -hex 32)` then `./mvnw spring-boot:run`.

Wait until you see something like `Started FinancialPlatformApplication`. Base URL: `http://localhost:8081`. Optional: `curl -s http://localhost:8081/actuator/health` should return `{"status":"UP"}`.

### 2. Register a user (get JWT)

```bash
curl -s -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password123","role":"CUSTOMER"}' | jq .
```

Copy the `token` from the response (or the whole response if you don’t have `jq`).

### 3. Login (alternative way to get JWT)

```bash
curl -s -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}' | jq .
```

Copy the `token` value.

### 4. Call protected APIs with the token

Set your token in a variable (replace with the actual token):

```bash
TOKEN="<paste-your-token-here>"
```

**Create a customer**

```bash
curl -s -X POST http://localhost:8081/customers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Jane Doe","email":"jane@example.com","retirementGoal":500000,"riskProfile":"MODERATE"}' | jq .
```

Note the customer `id` from the response (e.g. `1`).

**List customers**

```bash
curl -s http://localhost:8081/customers -H "Authorization: Bearer $TOKEN" | jq .
```

**Create a portfolio** (use the customer id from above, e.g. `1`)

```bash
curl -s -X POST http://localhost:8081/portfolio \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"customerId":1,"portfolioName":"My 401k"}' | jq .
```

**Get portfolios for a customer**

```bash
curl -s "http://localhost:8081/portfolio/customer/1" -H "Authorization: Bearer $TOKEN" | jq .
```

**Add an investment** (use a portfolio id from the list above, e.g. `1`)

```bash
curl -s -X POST http://localhost:8081/portfolio/1/investments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"assetName":"S&P 500 ETF","assetType":"ETF","amount":10000,"purchaseDate":"2024-01-15"}' | jq .
```

**Get investments in a portfolio**

```bash
curl -s "http://localhost:8081/portfolio/1/investments" -H "Authorization: Bearer $TOKEN" | jq .
```

**Get retirement projection**

```bash
curl -s "http://localhost:8081/retirement/projection/1" -H "Authorization: Bearer $TOKEN" | jq .
```

### 5. Test without `jq`

Omit `| jq .` to see raw JSON, for example:

```bash
curl -s -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test2@example.com","password":"password123"}'
```

### 6. Run automated tests (Maven)

```bash
cd backend
export JWT_SECRET=$(openssl rand -hex 32)
./mvnw test
```
