# Week 01 — Foundations

**Dates:** 06 Jul → ...
**Actual hours:** ~4

---

## Day 1 — 06 Jul

### What I built
- Spring Boot 3.5 project, Postgres in Docker, Flyway migration, 6 JPA entities mapped

### What slowed me down
- Port conflict between local Postgres and Docker — diagnosed with `lsof -i :5432`
- Bash 3 on Mac blocking SDKMAN install
- Spring Boot silently hijacking Docker Compose with its own credentials

### If an interviewer asked
> "I debugged a silent port conflict using `lsof` — local Postgres was intercepting connections meant for the Docker container."

### Next session
- Repositories + first custom query