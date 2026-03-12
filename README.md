# Retirement Investment Management Platform

Secure financial services platform for retirement planning, investment portfolio management, and customer financial workflows.

## Overview

- **Backend:** Spring Boot (Java 11) REST API with JWT authentication, Spring Data JPA, H2 (dev) / PostgreSQL (cloud), and Docker support.
- **Frontend:** Not included in this repository; the API is ready for a React or other client (see backend docs).

## Quick start (backend)

1. **Requirements:** Java 11+, Maven (or use the included `./mvnw`).

2. **Start the API:**

   ```bash
   cd backend
   ./run.sh
   ```

   Or set `JWT_SECRET` and run: `export JWT_SECRET=$(openssl rand -hex 32)` then `./mvnw spring-boot:run`.

3. **Verify:** Open [http://localhost:8080](http://localhost:8080) for a welcome message, or [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) for health.

4. **API docs:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Project structure

```
retirement-investment-management-platform/
├── backend/          # Spring Boot API (auth, customers, portfolios, investments, retirement projection)
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   ├── run.sh
│   └── README.md     # Full backend documentation
└── README.md         # This file
```

## Backend features

- **Auth:** Register and login with JWT; protected endpoints require `Authorization: Bearer <token>`.
- **Customers:** Create and list customers (name, email, retirement goal, risk profile).
- **Portfolios:** Create portfolios per customer; list portfolios by customer.
- **Investments:** Add and list investments (asset name, type, amount, purchase date) per portfolio.
- **Retirement projection:** GET projection (current savings, projected value, retirement age) by customer.
- **Health & docs:** Actuator health/info; Swagger UI at `/swagger-ui.html`.
- **Cloud:** Docker image and `cloud` profile for PORT and database env vars.

See [backend/README.md](backend/README.md) for full API reference, how to test with curl, and deployment notes.

## License

See [LICENSE](LICENSE) in this repository.
