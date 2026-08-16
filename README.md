# 💼 JobTrack - Intelligent Career Application & Pipeline Platform

[![Live Demo](https://img.shields.io/badge/Live_Demo-Railway_Production-00C7B7?style=for-the-badge&logo=railway&logoColor=white)](https://jobtrack-production-b276.up.railway.app)
[![Java 21](https://img.shields.io/badge/Java-21%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Spring Boot 3.3](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React 18](https://img.shields.io/badge/React-18.3-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-5.4-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)
[![Docker Compose](https://img.shields.io/badge/Docker_Compose-Multi--Service-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![MySQL 8.0](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/features/actions)
[![Tests](https://img.shields.io/badge/Tests-70%20Passed-brightgreen?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)

JobTrack is an enterprise-grade, containerized, full-stack career pipeline and job application tracking platform. Designed for modern engineering professionals and job seekers, JobTrack delivers interactive Kanban pipelines, multi-round interview tracking, follow-up reminder hubs, salary analytics, and RFC-4180 CSV export with secure JWT user isolation.

---

## 🚀 Live Production Demo

JobTrack is live and fully containerized in cloud production:

- **🌐 Live Web Application**: **[https://jobtrack-production-b276.up.railway.app](https://jobtrack-production-b276.up.railway.app)**
- **📖 Interactive Swagger UI Docs**: **[https://jobtrack-production-b276.up.railway.app/swagger-ui/index.html](https://jobtrack-production-b276.up.railway.app/swagger-ui/index.html)**
- **🩺 Actuator Health & Telemetry**: **[https://jobtrack-production-b276.up.railway.app/actuator/health](https://jobtrack-production-b276.up.railway.app/actuator/health)**

> **Quick Evaluation**: You can create a new account in 5 seconds via the web UI or log in with your credentials to explore the Kanban board, interview scheduler, analytics dashboard, and CSV exporter.

---

## ✨ Features Showcase

### 📊 Real-Time Pipeline Metrics & Ribbon
- **Live Status Telemetry**: Instant counters for total tracked opportunities, active interviews, screening rounds, and offers secured.
- **Salary Intelligence**: Computes average target compensation across active applications with midpoint range aggregation and currency awareness.

### 📋 Interactive Multi-View Dashboard
- **Kanban Board**: Drag-and-drop status progression through `APPLIED`, `SCREENING`, `INTERVIEWING`, `OFFER`, `REJECTED`, and `WITHDRAWN`.
- **Responsive Table & Grid Views**: Multi-column sorting by applied date, company name, priority level, and target salary.
- **Search & Filtering**: Multi-criteria search by company, job role, location, notes, workplace model (Remote / Hybrid / On-site), and employment type.

### 🗓️ Multi-Round Interview Management
- **Round-by-Round Scheduling**: Tracks HR Screening, Technical, System Design, Behavioral, and Managerial debriefs.
- **Interview Detail Logging**: Captures meeting links, interviewer contacts, technical questions asked, and debrief notes with full edit/delete lifecycle.

### 🔔 Follow-Up Reminders Hub
- **Action Tracking**: Never miss a thank-you note, portfolio submission, or offer response with dedicated due date indicators and overdue warning alerts.
- **One-Click Completion**: Interactive checkbox toggle to mark actions as pending or completed with immediate state synchronization.

### 📈 Career Pipeline Analytics
- **Progression Funnel**: Visual stage-by-stage conversion funnel showing screening-to-interview and interview-to-offer percentage rates.
- **Workplace & Salary Distribution**: Visual breakdown of remote vs. hybrid models and target compensation bands.

### 📥 RFC-4180 Compliant CSV Export
- **One-Click Export**: Downloads all user-isolated application data with UTF-8 BOM encoding for seamless Microsoft Excel and Google Sheets compatibility.

---

## 📸 Application Screenshots

### Dashboard

![JobTrack Dashboard](docs/screenshots/dashboard.png)

### Job Application Management

![Job Application Management](docs/screenshots/application.png)

### Interview Tracking

![Interview Tracking](docs/screenshots/interviews.png)

### Follow-Up Management

![Follow-Up Management](docs/screenshots/followups.png)

### Career Analytics

![Career Analytics](docs/screenshots/analytics.png)

---

## 🏗️ Architecture Overview

The application follows a decoupled, production-ready multi-tier architecture:

```
JobTrack/
├── .github/
│   └── workflows/
│       └── ci.yml                 # Automated CI/CD (Maven tests, Vite build, Docker images)
├── backend/
│   ├── src/                       # Spring Boot 3.3.4 (Java 21 LTS) REST API
│   │   ├── main/
│   │   │   ├── java/com/jobtrack/ # Controllers, Services, Security, Repositories, DTOs
│   │   │   └── resources/         # application.properties (parameterized env config)
│   │   └── test/                  # 70 Unit, Integration & Security Tests (MockMvc, H2)
│   ├── Dockerfile                 # Multi-stage build (Maven 3.9 -> Eclipse Temurin 21 JRE)
│   ├── .dockerignore
│   └── pom.xml
├── frontend/
│   ├── src/                       # React 18 SPA + Modern Glassmorphism UI (Lucide icons)
│   ├── nginx.conf                 # Production Nginx reverse proxy & SPA fallback
│   ├── Dockerfile                 # Multi-stage build (Node.js 20 -> Nginx 1.27 Alpine)
│   ├── .dockerignore
│   └── package.json
├── docker-compose.yml             # Orchestration: MySQL 8.0, Backend, Frontend
├── .env.example                   # Environment configuration template
├── .env                           # Local environment overrides (git-ignored)
└── README.md
```

---

## 🛠️ Technology Stack

| Layer | Technologies | Key Libraries & Features |
| :--- | :--- | :--- |
| **Frontend** | React 18, JavaScript (ES6+), Vite 5 | Tailwind-inspired Glassmorphism CSS, Lucide React Icons, React Router SPA |
| **Backend** | Spring Boot 3.3.4, Java 21 LTS | Spring Data JPA, Spring Security 6, JJWT (HMAC-SHA256), Spring Boot Actuator |
| **Database** | MySQL 8.0, H2 (Testing) | Hibernate 6 ORM, Flyway-ready JPA schema, Indexed queries, User-scoped foreign keys |
| **API Docs** | SpringDoc OpenAPI 3, Swagger UI | Interactive API playground with JWT Bearer authentication support |
| **Testing** | JUnit 5, Mockito, Spring Test, MockMvc | 70 automated tests covering unit, repository slice, service, and security layers |
| **DevOps** | Docker, Docker Compose, Nginx 1.27 | Multi-stage slim container builds, Alpine Linux, non-root user execution |
| **Cloud** | Railway.app Platform | Multi-service orchestration, Private networking, Container health probes |
| **CI/CD** | GitHub Actions | Automated Maven test execution, Vite build validation, Docker multi-stage builds |

---

## 🚀 Quick Start with Docker Compose

### 1. Configure Environment Variables
Copy the template to initialize your local `.env`:
```bash
cp .env.example .env
```

| Variable | Description | Default / Example |
| :--- | :--- | :--- |
| `DB_NAME` | MySQL database name | `jobtrack_db` |
| `DB_USER` | MySQL non-root username | `jobtrack_user` |
| `DB_PASSWORD` | MySQL user password | `jobtrack_pass` |
| `DB_ROOT_PASSWORD` | MySQL root administrative password | `root_secret_pass` |
| `DB_PORT` | Host port for MySQL access | `3306` |
| `BACKEND_PORT` | Host port for Spring Boot REST API | `8080` |
| `FRONTEND_PORT` | Host port for React/Nginx Web App | `3000` |
| `JWT_SECRET` | 256-bit secret key for HMAC-SHA256 tokens | `JobTrackSecretKeyForJwtAuthenticationMustBeAtLeast...` |
| `JWT_EXPIRATION_MS`| JWT token validity duration in milliseconds | `86400000` (24h) |

### 2. Start the Application Stack
Build images and start all containerized services:
```bash
docker compose up -d --build
```

### 3. Check Container Status & Health
```bash
docker compose ps
```

### 4. View Service Logs
```bash
# Stream all service logs
docker compose logs -f

# Stream specific service logs
docker compose logs -f backend
docker compose logs -f frontend
docker compose logs -f mysql
```

### 5. Stop the Application Stack
```bash
# Stop containers (preserves database data volume)
docker compose down

# Stop containers and remove persistent database volume
docker compose down -v
```

---

## 🌐 Application & API Endpoints

| Service / Component | Local URL | Live Cloud URL | Description |
| :--- | :--- | :--- | :--- |
| **Frontend Web App** | `http://localhost:3000` | [jobtrack-production-b276.up.railway.app](https://jobtrack-production-b276.up.railway.app) | React SPA served by Nginx |
| **Backend REST API** | `http://localhost:8080/api/v1` | `https://jobtrack-production-b276.up.railway.app/api/v1` | Spring Boot REST API |
| **Swagger UI Docs** | `http://localhost:8080/swagger-ui.html` | [Swagger Documentation](https://jobtrack-production-b276.up.railway.app/swagger-ui/index.html) | Interactive OpenAPI 3 Playground |
| **OpenAPI Specification**| `http://localhost:8080/v3/api-docs` | `/v3/api-docs` | Raw OpenAPI JSON spec |
| **Actuator Health** | `http://localhost:8080/actuator/health`| [Health Status](https://jobtrack-production-b276.up.railway.app/actuator/health) | Container Health & Liveness Probe |
| **Actuator Info** | `http://localhost:8080/actuator/info` | `/actuator/info` | Application metadata & metrics |

*(Note: In production and Docker, Nginx automatically reverse-proxies `/api/`, `/swagger-ui/`, and `/actuator/`)*

---

## ☁️ Cloud Deployment Architecture (Railway)

JobTrack is architected for zero-downtime, multi-container cloud deployment on Railway:

1. **Frontend Service**:
   - Built via multi-stage `frontend/Dockerfile` using Node.js 20 & Nginx 1.27 Alpine.
   - Exposes public HTTPS traffic and proxies `/api/*` requests internally to the backend.
2. **Backend Service**:
   - Built via multi-stage `backend/Dockerfile` using Eclipse Temurin 21 JRE on Alpine Linux.
   - Runs as a secure non-root user (`jobtrack`) with Actuator health probes.
   - Communicates privately with MySQL and Frontend over Railway's encrypted internal mesh network.
3. **Database Service**:
   - Managed MySQL 8.0 instance with persistent volume storage and automated backups.

---

## 🔒 Security & Best Practices

- **Zero Hardcoded Secrets**: All credentials (database user/passwords, JWT signing key) are passed dynamically via environment variables with safe fallbacks.
- **Strict User Isolation**: All data operations (Jobs, Interviews, Follow-Ups, Analytics) enforce strict user-level ownership validation in the service layer and database queries.
- **Least-Privilege Principle**: The Spring Boot backend runs under a non-root Alpine service user (`jobtrack:jobtrack`).
- **Multi-Stage Builds**: Development tooling, source code, and intermediate artifacts are stripped out of final production images.
- **Service Dependency & Healthchecks**:
  - `mysql` uses `mysqladmin ping` probe.
  - `backend` waits for MySQL healthy status and exposes an Actuator healthcheck probe.
  - `frontend` waits for backend healthy status and validates Nginx availability.
- **Data Persistence**: MySQL data persists across container restarts and updates via persistent Docker volumes.

---

## 🔄 GitHub Actions CI/CD Pipeline

The automated workflow located at [`.github/workflows/ci.yml`](.github/workflows/ci.yml) executes on every `push` and `pull_request` to `main`, `master`, and `develop`:

1. **Backend CI Job**:
   - Sets up Java 21 LTS (Eclipse Temurin)
   - Runs full Maven test suite (**70 unit, repository, service & integration tests**)
   - Packages production JAR artifact
2. **Frontend CI Job**:
   - Sets up Node.js 20.x
   - Installs dependencies via `npm ci`
   - Validates Vite production build (`npm run build`)
3. **Docker Validation Job**:
   - Verifies `docker compose config` syntax
   - Builds multi-stage backend and frontend Docker images

---

## 🧪 Local Development & Manual Testing

### Run Backend Unit & Integration Tests:
```bash
cd backend
# Windows:
.\mvnw.cmd clean test
# Linux / macOS:
./mvnw clean test
```

### Run Frontend Development Server:
```bash
cd frontend
npm install
npm run dev
```
Runs at `http://localhost:5173` with automatic API proxying to `http://localhost:8080`.

