# Demo Admin

Helper scripts and notes for running the backend (Spring Boot) and frontend (React + Vite) together from the project root.

## Prerequisites
- JDK 21
- Node.js 18+

## Quick start (both apps)
```bash
./dev.sh
```
The script starts the backend at http://localhost:8080 and the frontend at http://localhost:5173, installing frontend dependencies on first run. Stop both with Ctrl+C.

## Run manually
- Backend: `cd backend && ./gradlew bootRun`
- Frontend: `cd frontend && npm install && npm run dev`
