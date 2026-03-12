# Retirement Investment Management Platform – Backend

Spring Boot backend for the Retirement Investment Management Platform. Uses Java 17, Spring Security with JWT, Spring Data JPA, and H2 for development.

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

## Run

```bash
mvn spring-boot:run
```

Server: `http://localhost:8080`

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
