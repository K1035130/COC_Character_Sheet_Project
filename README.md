# COC Character Sheet Project

A tool for managing Call of Cthulhu (COC) tabletop RPG character sheets — create, view, edit, and delete characters, with derived stats (HP/MP/SAN) calculated automatically from core attributes.

This repository contains **two independent apps**:

| App | Location | Description |
|---|---|---|
| Web app (new) | [`backend/`](backend), [`frontend/`](frontend) | Multi-user, browser-based, deployable to Render |
| Desktop app (legacy) | [`ProjectStarter/`](ProjectStarter) | Original single-user Java Swing app, runs locally only |

The two apps do not share code or data. Pick whichever fits what you need — the web app for a hosted, multi-user setup, or the desktop app for a completely offline single-user tool.

---

## Web app

### Features

- Account registration/login (JWT-based, per-user data isolation).
- Create/view/edit/delete characters: name, occupation, age, gender.
- 9 core attributes (STR/DEX/CON/APP/POW/SIZ/INT/EDU/LUC), editable individually.
- Derived stats (HP/MP/SAN) recalculated automatically whenever CON/SIZ/POW change; cannot be set directly.
- **Random attribute allocation** — enter a total point budget (270–900) and randomly distribute it across the 9 core attributes, with each attribute constrained to 30–130.
- Skills: add/update/remove, with a live count of skills and total points spent.
- A radar chart visualizing the 9 core attributes.
- A **backstory** field for freeform character history, with a live character counter.
- Drag-and-drop (or click-to-browse) **avatar upload**, auto-resized/compressed client-side before saving.
- Per-character audit log of every change (attribute edits, skill changes, profile updates).

### Tech stack

- **Backend**: Spring Boot 3 (Java 21), MongoDB (Atlas free tier), Spring Security + JWT (stateless, BCrypt password hashing), Maven.
- **Frontend**: React + TypeScript (Vite), `react-router-dom`, `recharts`, plain CSS (no UI framework).

### Repo layout

```
backend/     Spring Boot REST API (Maven project, includes mvnw/mvnw.cmd)
frontend/    React + Vite single-page app
render.yaml  Render Blueprint (both services)
```

### Running locally

You'll need a MongoDB connection string — a free [MongoDB Atlas](https://www.mongodb.com/cloud/atlas/register) M0 cluster works well. Use a distinct database name for local dev (e.g. `coc_sheets_dev`) versus production (e.g. `coc_sheets_prod`) within the same cluster.

**Backend:**

```powershell
cd backend
copy .env.example .env   # fill in MONGODB_URI and a JWT_SECRET (32+ random bytes)
```

Set the environment variables and start the server (PowerShell):

```powershell
Get-Content .env | ForEach-Object { if ($_ -match '^([^=]+)=(.*)$') { Set-Item "env:$($matches[1])" $matches[2] } }
$env:SPRING_PROFILES_ACTIVE = "local"
.\mvnw.cmd spring-boot:run
```

The backend starts on `http://localhost:8080`.

**Frontend** (separate terminal):

```powershell
cd frontend
npm install
npm run dev
```

The frontend starts on `http://localhost:5173` and calls `http://localhost:8080/api` (see `.env.development`). Open it in a browser and register an account to get started.

### Running the backend tests

```powershell
cd backend
.\mvnw.cmd test
```

### Deploying to Render

The root `render.yaml` defines both services as a Render Blueprint:

1. `coc-sheet-backend` — a Docker-based Web Service built from `backend/Dockerfile`.
2. `coc-sheet-frontend` — a Static Site built from `frontend/` (`npm ci && npm run build`, publishing `dist/`).

To deploy:

1. Push this repo to GitHub.
2. In the Render dashboard, create a new Blueprint from the repo (Render reads `render.yaml` automatically).
3. On the `coc-sheet-backend` service, set the `MONGODB_URI` and `JWT_SECRET` secrets manually in the dashboard (they're marked `sync: false` in `render.yaml`, so nothing sensitive is committed to git). Point `MONGODB_URI` at a separate production database in the same Atlas cluster.
4. Deploy. The frontend's `VITE_API_BASE_URL` and the backend's `CORS_ALLOWED_ORIGIN` in `render.yaml` assume the default service names (`coc-sheet-backend`, `coc-sheet-frontend`) — update both if you rename the services.

**Free-tier note**: the backend web service spins down after ~15 minutes of inactivity, so the first request after idling has a cold-start delay (usually under a minute). The static frontend has no such delay.

---

## Desktop app (`ProjectStarter/`)

The original single-user Java Swing application. Character data is stored in a local JSON file (`ProjectStarter/data/characterRecord.json`) — no network, no account, no database.

### Features

- Add/view/edit/delete characters: name, occupation, age, gender, attributes, skills.
- Derived stats (HP/MP/SAN) recalculated automatically from core attributes.
- Save/load the character list to/from a JSON file.
- An in-memory event log recording every change made during a session.

### Running it

This project has no build tool (no Maven/Gradle) — it's compiled and run directly from source, which is how it originally shipped. `ProjectStarter/.project` and `ProjectStarter/.classpath` are included so VS Code's Java extension picks up the source folders and the jars in `ProjectStarter/lib/` automatically; open the `ProjectStarter/` folder in VS Code and run `src/main/ui/Main.java`.

Tests (JUnit 5) live under `ProjectStarter/src/test/` and can be run from VS Code's test explorer, or via the bundled `ProjectStarter/lib/junit-platform-console-standalone-1.10.2.jar`.
