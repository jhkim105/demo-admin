#!/usr/bin/env bash
# Helper to run backend and frontend together from the project root.
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

cleanup() {
  [[ -n "${BACKEND_PID:-}" ]] && kill "$BACKEND_PID" >/dev/null 2>&1 || true
  [[ -n "${FRONTEND_PID:-}" ]] && kill "$FRONTEND_PID" >/dev/null 2>&1 || true
}

trap cleanup EXIT INT TERM

echo "Starting backend (http://localhost:8080)..."
(cd "$ROOT/backend" && ./gradlew bootRun) &
BACKEND_PID=$!

echo "Installing frontend deps (npm install) if needed..."
(cd "$ROOT/frontend" && npm install >/dev/null) 

echo "Starting frontend (http://localhost:5173)..."
(cd "$ROOT/frontend" && npm run dev) &
FRONTEND_PID=$!

echo "Backend PID: $BACKEND_PID"
echo "Frontend PID: $FRONTEND_PID"
echo "Press Ctrl+C to stop both."

wait "$BACKEND_PID" "$FRONTEND_PID"
