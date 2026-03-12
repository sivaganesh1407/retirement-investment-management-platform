# Package Architecture

## `com.financial.platform`

### `controller`
REST API layer. Exposes HTTP endpoints, validates request DTOs, and delegates business logic to services. Returns HTTP status and response bodies.

### `service`
Business logic layer. Contains use-case logic, orchestration, and transactions. Services call repositories and other services. No HTTP or persistence details here.

### `repository`
Data access layer. Spring Data JPA repositories for CRUD and custom queries against the database. One repository per aggregate/entity type.

### `model`
Domain and persistence entities. JPA entities mapped to database tables. Also request/response DTOs if kept in the same package or a dedicated `dto` subpackage.

### `security`
Authentication and authorization: JWT utilities (generate/validate tokens), JWT filter, and any auth-related components used by Spring Security.

### `config`
Spring configuration: Security config (which URLs are protected, CORS, etc.), other beans (e.g. `PasswordEncoder`, `AuthenticationManager`), and app-wide settings.
