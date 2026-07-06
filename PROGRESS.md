# Progress Tracker — Job Application Tracker Project

**Goal:** Mid-level Fullstack Java role. 12 weeks, 10-15 hrs/week.
**Start date:** _fill in_
**Target finish date:** _fill in (start + 12 weeks)_

## How to use this file
- Update the status table every Sunday during your weekly review.
- Each week links to a devlog entry in `devlog/week-NN.md` — write that entry every Sunday too (10-15 min, don't skip it, it's your interview prep material later).
- "On track?" is the one honest binary signal. If a week is ❌ two weeks in a row, stop and re-read the "cut order" section in the plan before adding more scope.

## Status table

| Week | Theme | Status | On track? | Devlog |
|---|---|---|---|---|
| 1 | Foundations (Spring Boot skeleton, Postgres, Flyway, Application CRUD) | Not started | — | [week-01](devlog/week-01.md) |
| 2 | Auth (Spring Security + JWT, first Testcontainers test) | Not started | — | [week-02](devlog/week-02.md) |
| 3 | Domain depth (Interview/Tag/ResumeVersion, state machine, exception handling) | Not started | — | [week-03](devlog/week-03.md) |
| 4 | Querying & API polish (pagination/filtering, dashboard JPQL, OpenAPI) | Not started | — | [week-04](devlog/week-04.md) |
| 5 | MinIO + file upload | Not started | — | [week-05](devlog/week-05.md) |
| 6 | RabbitMQ pipeline pt.1 (broker + publish) | Not started | — | [week-06](devlog/week-06.md) |
| 7 | RabbitMQ pipeline pt.2 (parsing chain, consumer, DLQ) | Not started | — | [week-07](devlog/week-07.md) |
| 8 | Scheduled reminders + Angular start (auth flow, routing) | Not started | — | [week-08](devlog/week-08.md) |
| 9 | Angular core screens (list/board, detail view) | Not started | — | [week-09](devlog/week-09.md) |
| 10 | Angular polish + CI (GitHub Actions) | Not started | — | [week-10](devlog/week-10.md) |
| 11 | Deploy (Railway/Fly.io, live URL) | Not started | — | [week-11](devlog/week-11.md) |
| 12 | README, polish, interview-readiness | Not started | — | [week-12](devlog/week-12.md) |

Status values: `Not started` / `In progress` / `Done` / `Slipped`
On track values: ✅ / ⚠️ / ❌

## Load-bearing pieces (protect these if time gets tight)
- RabbitMQ async pipeline (weeks 6-7) — your strongest interview story
- Status state machine (week 3) — shows you don't just CRUD
- Testcontainers integration tests — shows testing maturity

## Cut order if behind schedule (in order, first to cut first)
1. Angular polish (week 10 extras)
2. Scheduled reminders (week 8)
3. ResumeVersion multi-versioning depth (keep basic upload, drop versioning UI)
4. Live deploy (last resort — a great local demo beats a broken live one)

## Running issue count
- Total issues planned: _fill in after running create_issues.sh_
- Closed so far: _update weekly_
