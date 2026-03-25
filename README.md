# Retirement Investment Management Platform

**Disclaimer:** This is an independent, educational sample project. It is not affiliated with, endorsed by, or produced for any employer or commercial entity. Use at your own risk; it is not financial or legal advice.

Secure financial services platform for retirement planning, investment portfolio management, and customer financial workflows.

## Overview

- **Backend:** Spring Boot (Java 11) REST API with JWT authentication, Spring Data JPA, H2 (dev) / PostgreSQL (cloud), and Docker support.
- **Frontend:** React (Vite) app for login, customers, portfolios, investments, and retirement projections.

## Quick start (full stack)

1. **Start the backend** (from project root):

   ```bash
   cd backend
   ./run.sh
   ```

   Or with Docker: `export JWT_SECRET=$(openssl rand -hex 32)` then `docker compose up -d`.  
   Wait until the API is up (e.g. [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)).

2. **Start the frontend** (new terminal):

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

   The frontend directory includes `.npmrc` with `registry=https://registry.npmjs.org/` so dependencies resolve from the public npm registry only (no private registry or auth required for these packages).

3. **Open:** [http://localhost:3000](http://localhost:3000) — register or login, then use the app.

**Backend only:** API at [http://localhost:8081](http://localhost:8081), docs at [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html).

## Project structure

```
retirement-investment-management-platform/
├── backend/          # Spring Boot API (auth, customers, portfolios, investments, retirement projection)
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   ├── docker-compose.yml
│   ├── run.sh
│   └── README.md
├── frontend/         # React (Vite) UI
│   ├── src/
│   ├── package.json
│   └── README.md
└── README.md
```

## Features

- **Frontend:** React app — login/register, manage customers, portfolios, investments; view retirement projection. Dev server: `localhost:3000`; API: `localhost:8081`. CORS allows those origins (and `5173` if you change the Vite port).
- **Backend:** JWT auth; customers, portfolios, investments, retirement projection APIs; Actuator health/info; Swagger at `/swagger-ui.html`; Docker and `cloud` profile.

See [backend/README.md](backend/README.md) for API reference and [frontend/README.md](frontend/README.md) for frontend run/build.

## License

See [LICENSE](LICENSE) in this repository.
