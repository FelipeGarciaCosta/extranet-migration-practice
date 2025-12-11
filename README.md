- # Extranet (DSP)

A full-stack CRM-style application — Spring Boot backend and Angular frontend.

I built this project with the guide of my internship tutor while migrating legacy patterns to a modern stack (from older Java/Spring approaches to Java 21, Spring Boot 3.x and Angular 20). During the process I learned to implement JWT-based authentication with Spring Security, configure stateless CORS-enabled APIs, use JPA with MySQL, and build a modern Angular application with standalone components, interceptors and reactive services. The project also improved my debugging, configuration and full-stack deployment skills.

**Project Overview**

- **Name:** Extranet (DSP)
- **Type:** Full-stack web application (Spring Boot backend + Angular frontend)
- **Purpose:** Simple CRM-like administration app exposing client data and authentication via JWT.

**Technology Stack**
- **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA, JJWT (JWT), MySQL
- **Frontend:** Angular 20 (standalone components), TypeScript, RxJS

**Repository Layout**
- **backend-dsp**: Spring Boot API and services. See [backend-dsp/pom.xml](backend-dsp/pom.xml#L1).
- **frontend-dsp**: Angular SPA. See [frontend-dsp/package.json](frontend-dsp/package.json#L1) and [frontend-dsp/angular.json](frontend-dsp/angular.json#L1).

**Backend — Key Points**
- **Application entry:** [backend-dsp/src/main/java/com/dsp/backend/BackendApplication.java](backend-dsp/src/main/java/com/dsp/backend/BackendApplication.java#L1).
- **Security & JWT:** Configuration and filter are in [backend-dsp/src/main/java/com/dsp/backend/config/SecurityConfig.java](backend-dsp/src/main/java/com/dsp/backend/config/SecurityConfig.java#L1) and [backend-dsp/src/main/java/com/dsp/backend/security/jwt](backend-dsp/src/main/java/com/dsp/backend/security/jwt).
- **Initial data:** [backend-dsp/src/main/java/com/dsp/backend/config/DataInitializer.java](backend-dsp/src/main/java/com/dsp/backend/config/DataInitializer.java#L1) creates default roles and an `admin` user (password in code: `mipasslarga`, stored hashed).
- **Config file:** See database and JWT settings in [backend-dsp/src/main/resources/application.properties](backend-dsp/src/main/resources/application.properties#L1).

Backend notable endpoints (controllers)
- **Auth:** `POST /api/auth/login` — implemented in [backend-dsp/src/main/java/com/dsp/backend/controller/AuthController.java](backend-dsp/src/main/java/com/dsp/backend/controller/AuthController.java#L1). Returns a JWT token and user roles.
- **Clientes:** CRUD-lite endpoints in [backend-dsp/src/main/java/com/dsp/backend/controller/ClientesController.java](backend-dsp/src/main/java/com/dsp/backend/controller/ClientesController.java#L1):
	- `GET /api/clientes` — list clients
	- `GET /api/clientes/{codCli}` — get client by code
	- `PATCH /api/clientes/activar/{codCli}` — activate client
	- `PATCH /api/clientes/desactivar/{codCli}` — deactivate client

**Frontend — Key Points**
- **App bootstrap:** [frontend-dsp/src/main.ts](frontend-dsp/src/main.ts#L1) uses `bootstrapApplication` (Standalone Components).
- **Routing:** [frontend-dsp/src/app/app.routes.ts](frontend-dsp/src/app/app.routes.ts#L1) defines routes for `login`, `dashboard`, `clientes`, `contactos`.
- **Auth service / token storage:** [frontend-dsp/src/app/services/auth.service.ts](frontend-dsp/src/app/services/auth.service.ts#L1) calls the backend auth API, stores token and user in `localStorage` under `currentUser`.
- **HTTP interceptor:** [frontend-dsp/src/app/interceptors/auth.interceptor.ts](frontend-dsp/src/app/interceptors/auth.interceptor.ts#L1) attaches `Authorization: Bearer <token>` to outgoing requests when present.
- **Important components:**
	- Login: [frontend-dsp/src/app/components/login/login.ts](frontend-dsp/src/app/components/login/login.ts#L1)
	- Clientes (list, filters, create): [frontend-dsp/src/app/components/clientes/clientes.ts](frontend-dsp/src/app/components/clientes/clientes.ts#L1)

**Running Locally**

Backend (Linux/macOS):

```
cd backend-dsp
./mvnw spring-boot:run
```

Backend (Windows CMD / PowerShell):

```
cd backend-dsp
mvnw.cmd spring-boot:run
```

Notes:
- Database connection and JWT secret are in [backend-dsp/src/main/resources/application.properties](backend-dsp/src/main/resources/application.properties#L1). By default it points to MySQL at `jdbc:mysql://localhost:3305/extranet` with username `extranet`.
- The `DataInitializer` bean will create default roles and a test admin user if the DB is empty.

Frontend:

```
cd frontend-dsp
npm install
npm start
```

By default the frontend expects the backend API base used in `AuthService` (see [frontend-dsp/src/app/services/auth.service.ts](frontend-dsp/src/app/services/auth.service.ts#L1)). Update the `apiUrl` in that file to point to your backend host if necessary.

**Default test credentials**
- Username: `admin`
- Password: `mipasslarga` (created by `DataInitializer` if DB empty)

**API / Integration notes**
- JWT is required for protected endpoints. The frontend stores the JWT in `localStorage` and the interceptor adds it to requests.
- CORS is configured in `SecurityConfig` to allow `http://localhost:4200` and `http://localhost:3000`.

**Development tips**
- To change DB connection, edit [backend-dsp/src/main/resources/application.properties](backend-dsp/src/main/resources/application.properties#L1).
- To change frontend API base URL, edit [frontend-dsp/src/app/services/auth.service.ts](frontend-dsp/src/app/services/auth.service.ts#L1).

**Contributing**
- Open an issue or pull request with a clear description and steps to reproduce.

**License**
- Add your preferred license file (e.g. `LICENSE`) before publishing to GitHub.

--
Generated README summarizing both modules. Run backend then frontend and update configuration values as needed.
