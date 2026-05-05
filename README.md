# Teaching Evaluation — Backend

REST API for the Teaching Evaluation web application. Handles user authentication, evaluation data, report storage, and email notifications.

**Paired frontend:** https://github.com/sri-tella/TE-frontend  
**Live API:** https://te-backend-production.up.railway.app

---

## What does this API do?

- Authenticates users and manages three roles: **Observer**, **Instructor**, **Admin**
- Stores evaluation forms, activity logs, and class information
- Saves and serves PDF evaluation reports
- Sends email notifications via Gmail SMTP
- Manages role requests and admin-created accounts

---

## Tech Stack

| Tool | Purpose |
|------|---------|
| [Spring Boot 3.3](https://spring.io/projects/spring-boot) | Java web framework |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa) | Database access (ORM) |
| [MySQL](https://www.mysql.com) | Relational database |
| [Spring Security](https://spring.io/projects/spring-security) | Auth & password hashing |
| [Spring Mail](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/mail/package-summary.html) | Email via Gmail SMTP |
| [Apache PDFBox](https://pdfbox.apache.org) | PDF processing |
| [Lombok](https://projectlombok.org) | Boilerplate reduction |
| [Maven](https://maven.apache.org) | Build tool |

---

## Prerequisites

- **Java 17** or higher — [download here](https://adoptium.net)
- **Maven** (or use the included `./mvnw` wrapper — no install needed)
- **MySQL 8** running locally

Check your Java version:
```bash
java --version   # should be >= 17
```

---

## Local Setup

### 1. Clone the repository
```bash
git clone https://github.com/sri-tella/TE-backend.git
cd TE-backend
```

### 2. Create a local MySQL database

Open MySQL and run:
```sql
CREATE DATABASE teachapp;
```

The default local credentials are `root` / `12345`. If yours are different, edit `src/main/resources/application-local.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/teachapp
spring.datasource.username=root
spring.datasource.password=your_password
```

> Hibernate will automatically create all tables on first run (`ddl-auto=update`), so you don't need to run any SQL scripts.

### 3. Start the server in local mode
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

On Windows (Command Prompt):
```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

The API will be available at **http://localhost:8080**

---

## Available Scripts

```bash
# Run in local mode (uses local MySQL)
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"

# Build a production JAR
./mvnw clean package

# Run the built JAR
java -jar target/TeBackend-0.0.1-SNAPSHOT.jar

# Run tests
./mvnw test
```

---

## Project Structure

```
src/main/java/TeApp/TeBackend/
├── controller/     # REST endpoints (what the frontend calls)
├── entity/         # Database table models (JPA entities)
├── repository/     # Database queries (Spring Data interfaces)
├── service/        # Business logic
├── dto/            # Data shapes sent/received by the API
└── config/         # Security, CORS configuration

src/main/resources/
├── application.properties          # Shared config (loaded always)
├── application-local.properties    # Local dev overrides (gitignored)
└── application-prod.properties     # Production overrides (uses Railway env vars)
```

---

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/signup` | Register a new user |
| POST | `/api/auth/login` | Login, returns user info and role |
| GET | `/api/evaluations/` | Get all evaluations |
| GET | `/api/evaluations/{id}` | Get one evaluation |
| POST | `/api/evaluations/save` | Create or update an evaluation |
| POST | `/api/evaluations/start` | Start a new evaluation session |
| GET | `/api/reports/` | Get all reports |
| GET | `/api/reports/{id}` | Get one report |
| POST | `/api/reports/save-pdf` | Upload a PDF report |
| GET | `/api/reports/{id}/pdf` | Download a PDF report |
| DELETE | `/api/reports/{id}` | Delete a report |
| POST | `/api/admins/` | Create a new admin user |
| GET | `/api/classes/` | Get class information |
| GET | `/api/recommendations/` | Get recommendations |
| GET | `/api/notifications/` | Get notifications |

---

## Configuration

The app uses **Spring profiles** to switch between environments.

### Profiles

| Profile | When used | Config file |
|---------|-----------|-------------|
| `local` | Local development | `application-local.properties` |
| `prod` | Railway production | `application-prod.properties` |

The active profile is set in `application.properties`:
```properties
spring.profiles.active=prod
```

Change it to `local` for local development, or override it via the command-line argument shown above.

### Environment Variables (Production)

In production, Railway provides these automatically via the MySQL plugin:

| Variable | Description |
|----------|-------------|
| `MYSQLHOST` | Database host |
| `MYSQLPORT` | Database port |
| `MYSQLDATABASE` | Database name |
| `MYSQLUSER` | Database username |
| `MYSQLPASSWORD` | Database password |

You must set these manually in Railway:

| Variable | Description |
|----------|-------------|
| `MAIL_PASSWORD` | Gmail app password for sending emails |
| `MAIL_USERNAME` | Gmail address (optional, has a default) |
| `ALLOWED_ORIGINS` | Comma-separated list of allowed frontend URLs |

---

## CORS Configuration

By default, these frontend origins are allowed:

```
https://teachingeval.netlify.app
https://teaching-evaluation.netlify.app
http://localhost:3000
http://localhost:5173
```

To add more origins, set the `ALLOWED_ORIGINS` environment variable:
```
ALLOWED_ORIGINS=https://your-domain.com,http://localhost:5173
```

---

## Deployment

The backend is deployed on **Railway**.

- Every push to `main` triggers an automatic redeploy
- Railway builds the app with `./mvnw clean package`
- The MySQL database is a Railway-managed plugin (credentials injected automatically)
- All secrets are stored as Railway environment variables — never committed to git

---

## Common Issues

**`java: error: release version 17 not supported`**
Your Java version is too old. Install Java 17+.

**`Communications link failure` (MySQL connection error)**
Make sure MySQL is running and the credentials in `application-local.properties` are correct.

**`Table doesn't exist`**
Make sure `spring.jpa.hibernate.ddl-auto=update` is set. Restart the server — Hibernate will create the tables.

**Emails not sending**
Check that `MAIL_PASSWORD` is set to a valid [Gmail App Password](https://support.google.com/accounts/answer/185833) (not your regular Gmail password).
