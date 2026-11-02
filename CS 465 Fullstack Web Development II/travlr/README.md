# Travlr Getaways - Full-Stack MEAN Security & SPA Application (CS-465 Module 7)

This repository contains the complete Full-Stack MEAN (MongoDB, Express, Angular, Node.js) application for **CS-465 Module 7**, implementing JWT authentication, Passport Local Strategy, Mongoose schemas, and an Angular Admin Single Page Application (SPA).

---

## Quick Start Evaluation Guide for Evaluators

To test and grade this application in a environment with Node.js (v18+), npm, and a local Mongoose/MongoDB connection:

### Step 1: Install Dependencies
```bash
# In the travlr directory:
npm install

# In the app_admin directory:
cd app_admin
npm install
cd ..
```

### Step 2: Seed the Database
Seed the initial trip records into MongoDB (`mongodb://127.0.0.1/travlr`):
```bash
npm run seed
```

### Step 3: Start Backend API & Public Customer Server
From the `travlr/` root directory:
```bash
npm start
```
* Server starts on **`http://localhost:3000`**
* **Public Customer Page:** `http://localhost:3000/travel`
* **REST API Base URL:** `http://localhost:3000/api`

### Step 4: Start Angular Admin SPA
In a separate terminal tab/window, navigate to `travlr/app_admin/`:
```bash
npm start
```
* Angular Admin SPA starts on **`http://localhost:4200`**
* **Admin Login & Dashboard:** `http://localhost:4200/login`

---

## Technical Architecture & Security Features

* **Backend (`app_api`):** Express + Mongoose + Passport Local Strategy + PBKDF2 password hashing (16-byte salt, sha512) + JWT payload signing/verification.
* **Route Protection:** Express middleware (`express-jwt`) guards `POST /api/trips` and `PUT /api/trips/:tripCode`. Unauthenticated requests return HTTP 401 Unauthorized.
* **Frontend SPA (`app_admin`):** Angular + RxJS + Reactive Forms + `AuthenticationService` managing JWT tokens in `localStorage` (`travlr-token`).
* **HTTP Interception:** `TripDataService` attaches `Authorization: Bearer <token>` headers to protected write operations.
* **Navigation Security:** `AuthGuard` prevents unauthorized access to `/add-trip` and `/edit-trip`.
* **Dynamic UI:** `NavbarComponent` dynamically updates Login / Logout button state based on authentication state.
