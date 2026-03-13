# Retirement Investment Platform – Frontend

React (Vite) frontend for the Retirement Investment Management Platform. Login, manage customers, portfolios, investments, and view retirement projections.

## Requirements

- Node.js 18+
- Backend running at `http://localhost:8080` (or set `VITE_API_URL`)

## Run

```bash
npm install
npm run dev
```

Open [http://localhost:5173](http://localhost:5173). Use **Register** to create an account, then **Login**. The backend must be running and CORS is configured for `localhost:5173`.

## Build

```bash
npm run build
```

Output is in `dist/`. For production, point `VITE_API_URL` to your API base URL when building.

## Features

- Login / Register (JWT)
- List and create customers
- Per customer: view retirement projection, portfolios, investments; add portfolios and investments
