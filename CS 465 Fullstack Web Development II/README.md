# Final Journal Entry
## Journal Entry: Angular SPA Architecture, Functionality, and Testing

Working on the Travlr Getaways project helped me understand the difference between a traditional server-rendered web application and a single-page application (SPA). The customer-facing side of the application uses Express with Handlebars to render HTML pages on the server. The administrator side is different because it uses Angular to create a client-side application that communicates with the same Express/Node.js backend and MongoDB database.

### Angular Project Structure vs. Express HTML

The Angular project is structured more around components and client-side logic than the Express customer-facing application. With Express and Handlebars, the server is responsible for receiving the request, processing it, and sending an HTML page back to the browser. The templates are organized around pages and views, and Express controls much of the flow.

Angular breaks the application into reusable components. Instead of having the server generate an entirely new HTML page for every request, Angular loads the application and then uses components, services, routing, and data binding to change what the user sees. This makes the administrator application feel more like an actual application rather than a collection of separate web pages.

The Angular application also communicates with the API directly. For example, the administrator can retrieve the travel information from the backend, display it in an Angular component, and then send changes back through an API request. The backend still handles the database operations through Node.js, Express, Mongoose, and MongoDB, but Angular is responsible for much more of the user interface logic.

### Advantages and Disadvantages of an SPA

One of the biggest advantages of an SPA is that the page does not have to completely reload every time the user performs an operation. Angular can update a component when data changes, which makes the application feel faster and more responsive. Client-side routing also allows the user to move between different parts of the administrator application without constantly requesting completely new HTML pages.

Another advantage is reusability. Angular components and services can be used in multiple places instead of duplicating the same HTML and JavaScript logic. Two-way data binding is also useful for administration because the information displayed in a form can be connected directly to the application's data.

The disadvantage is that an SPA is more complicated than a basic web application. There are more moving parts to keep track of, including Angular components, services, routing, API requests, authentication, and the Express backend. Debugging can also be more difficult because a problem could be in the Angular code, the API, the Express server, or the database.

A simple web application interaction might involve submitting a form and waiting for the server to return a completely new page. An SPA can make the same interaction without leaving the current page. In the Travlr application, this gives the administrator the ability to manage travel information through the Angular interface while communicating with the API in the background.

### Testing the SPA and API

Testing the SPA means testing more than just whether the Angular page looks correct. I need to make sure the Angular application can actually communicate with the Express API and that the API is correctly communicating with MongoDB.

For a GET request, I can start by making sure the backend is running and the database contains the expected travel data. The Angular application then makes the API request and receives the data. I can verify that the information displayed in the administrator interface matches what is actually stored in MongoDB.

For a PUT request, I can change a piece of travel information through the Angular administrator interface and submit the update. The API should receive the request, identify the correct database record, update it through Mongoose, and return the appropriate response. I can then verify the result by retrieving the data again and making sure the change persisted in MongoDB.

There are several places where this process could fail. The Angular application could have the wrong API URL, the API route could be incorrect, or the request could contain data in the wrong format. The backend could also return an HTTP error if a record does not exist or if validation fails. There could also be problems connecting to MongoDB. On the client side, I would expect errors such as a failed HTTP request, incorrect data binding, or a component not updating when the API response is received.

One thing I found useful about this project is that testing the application also means thinking about the entire path of the data:

**Angular component → API request → Express route → Mongoose → MongoDB → response → Angular component**

If something goes wrong, I can use that path to narrow down where the problem is occurring instead of assuming the problem is with Angular itself.

### Questions for Future SPA Projects

One question I still have is how much application logic should really belong in Angular versus the backend. It is easy to put more functionality into the client because Angular makes it convenient, but I want to better understand where the boundary should be between client-side logic and server-side business logic.

I also want to learn more about handling authentication and authorization in a larger SPA. The administrator application needs to communicate with protected backend functionality, so understanding how to securely maintain a user's session and prevent unauthorized API requests will be important in future projects.

Finally, I would like to get better at testing SPAs systematically. I understand how to manually test GET and PUT operations and follow the data from Angular through the API to MongoDB, but I want to learn more about automated testing for Angular components, services, and API interactions. That would make it easier to catch problems before they reach the user.

# Travlr Getaways - Full Stack Application & Angular SPA (Module 6)

## Introduction

This repository contains the full-stack web application for **Travlr Getaways**, developed as part of **CS 465 Full Stack Development**. 

Module 6 integrates an **Angular Single Page Application (SPA)** for administrative trip management alongside the Express backend, Handlebars customer portal, and MongoDB database.

The application architecture is structured into distinct backend, frontend, and database layers:

- **Backend Layer**
  - **Node.js Runtime**: Provides the event-driven server execution environment.
  - **Express Web Framework**: Manages HTTP routing, middleware, CORS, and server configuration ([`travlr/app.js`](travlr/app.js)).
  - **RESTful API Service**: Exposes API endpoints under `/api/trips` supporting `GET`, `POST`, and `PUT` operations ([`travlr/app_api/`](travlr/app_api/)).

- **Frontend Layers**
  - **Customer Web Portal**: Server-side rendered (SSR) travel catalog built with Handlebars (`hbs`) templates (`http://localhost:3000/travel`).
  - **Administrative SPA**: Single Page Application built with Angular ([`travlr/app_admin`](travlr/app_admin)) running on `http://localhost:4200` (or statically mounted at `/admin`) for trip CRUD management.

- **Database & Container Infrastructure**
  - **MongoDB Database**: Document database storing trip schemas and records.
  - **Docker Containerization**: Containerized database service managed by Docker Compose ([`cs465-dev-env`](cs465-dev-env)) listening on `localhost:27017`.

---

## Architecture Overview

```
+-------------------------------------------------------------------------+
|                        Travlr Getaways Architecture                     |
+-------------------------------------------------------------------------+
|                                                                         |
|  +---------------------------+         +-----------------------------+  |
|  |   Customer Web Browser    |         |     Admin SPA Portal        |  |
|  |  (Handlebars /travel UI)  |         | (Angular 16+ app_admin)     |  |
|  +-------------+-------------+         +--------------+--------------+  |
|                |                                      |                 |
|                | HTTP GET                             | HTTP GET/POST/  |
|                v                                      |      PUT        |
|  +----------------------------------------------------+--------------+  |
|  |                      Express Server (:3000)                       |  |
|  |  +--------------------------+    +-----------------------------+  |  |
|  |  |  app_server (Handlebars) |    |  app_api (REST controllers) |  |  |
|  |  +--------------------------+    +--------------+--------------+  |  |
|  +-------------------------------------------------|-----------------+  |
|                                                    | Mongoose           |
|                                                    v                    |
|                                   +----------------------------------+  |
|                                   |  MongoDB Container (travlr DB)   |  |
|                                   |       (cs465-dev-env :27017)     |  |
|                                   +----------------------------------+  |
+-------------------------------------------------------------------------+
```

---

## Frontend Angular SPA Architecture (`app_admin`)

The administrative Single Page Application is located in [`travlr/app_admin`](travlr/app_admin) and features:

| Component / Module | Path | Description |
| :--- | :--- | :--- |
| **`TripListingComponent`** | [`travlr/app_admin/src/app/trip-listing/`](travlr/app_admin/src/app/trip-listing/) | Displays interactive trip list and provides triggers to add or edit trips. |
| **`TripCardComponent`** | [`travlr/app_admin/src/app/trip-card/`](travlr/app_admin/src/app/trip-card/) | Renders individual trip cards displaying image, price, resort, and description. |
| **`AddTripComponent`** | [`travlr/app_admin/src/app/add-trip/`](travlr/app_admin/src/app/add-trip/) | Reactive Form collecting trip details to create a new record via `POST /api/trips`. |
| **`EditTripComponent`** | [`travlr/app_admin/src/app/edit-trip/`](travlr/app_admin/src/app/edit-trip/) | Reactive Form pre-populated with existing trip data (via `localStorage` `tripCode`) to modify a trip record via `PUT /api/trips/:tripCode`. |
| **`NavbarComponent`** | [`travlr/app_admin/src/app/navbar/`](travlr/app_admin/src/app/navbar/) | Header navigation component for the SPA. |
| **`TripDataService`** | [`travlr/app_admin/src/app/services/trip-data.service.ts`](travlr/app_admin/src/app/services/trip-data.service.ts) | Angular `HttpClient` service consuming the Express REST API endpoints (`getTrips`, `getTrip`, `addTrip`, `updateTrip`). |
| **`AppRoutingModule`** | [`travlr/app_admin/src/app/app-routing.module.ts`](travlr/app_admin/src/app/app-routing.module.ts) | Angular Client-Side Router mapping `/list-trips`, `/add-trip`, `/edit-trip`. |

---

## Development Environment Setup (`cs465-dev-env`)

Local development uses a Docker container managed by Docker Compose for MongoDB.

### Prerequisites

Make sure you have installed:
- **Node.js** (v18 or higher) - [Download here](https://nodejs.org/)
- **npm** (comes with Node.js)
- **Docker & Docker Compose** - [Download Docker Desktop](https://www.docker.com/products/docker-desktop/)
- **Git** - [Download here](https://git-scm.com/)

---

### Step 1: Start MongoDB via Docker Compose

1. Clone or ensure `cs465-dev-env` is present in the repository root:
   ```bash
   git clone https://github.com/uturuncuayaku/cs465-dev-env.git cs465-dev-env
   ```

2. Start the MongoDB database container:
   ```bash
   cd cs465-dev-env
   docker compose up -d
   ```
   *This starts a MongoDB container on `localhost:27017` named `travlr-mongodb`.*

---

### Step 2: Install Dependencies & Seed Database

1. Navigate to the `travlr` application directory:
   ```bash
   cd ../travlr
   ```

2. Install Node.js dependencies:
   ```bash
   npm install
   ```

3. Seed MongoDB with initial trip data:
   ```bash
   node app_api/models/seed.js
   ```

---

### Step 3: Build & Run the Angular SPA Admin Portal

1. Navigate to the `app_admin` directory:
   ```bash
   cd app_admin
   ```

2. Install Angular SPA dependencies:
   ```bash
   npm install
   ```

3. Build the Angular application for Express static serving:
   ```bash
   npm run build
   ```
   *(Or run standalone dev server via `ng serve` on `http://localhost:4200`)*

---

### Step 4: Start the Express Application Server

From the `travlr` directory, run:
```bash
npm start
```

The Express application will listen on **`http://localhost:3000`**.

---

## Available Application Routes

| Interface / API | URL | Description |
| :--- | :--- | :--- |
| **Public Customer Website** | `http://localhost:3000/travel` | Handlebars SSR travel trip catalog. |
| **Admin SPA Portal** | `http://localhost:4200/` or `http://localhost:3000/admin` | Angular SPA administrative management dashboard. |
| **REST API - Get All Trips** | `GET http://localhost:3000/api/trips` | Returns JSON array of all active trip records. |
| **REST API - Get Single Trip** | `GET http://localhost:3000/api/trips/:tripCode` | Returns JSON object of single trip matching `:tripCode`. |
| **REST API - Create Trip** | `POST http://localhost:3000/api/trips` | Inserts a new trip document into MongoDB. |
| **REST API - Update Trip** | `PUT http://localhost:3000/api/trips/:tripCode` | Updates existing trip document matching `:tripCode`. |

---

## Useful Commands

| Command | Working Directory | Action |
| :--- | :--- | :--- |
| `docker compose up -d` | `cs465-dev-env/` | Start local MongoDB container |
| `docker compose down` | `cs465-dev-env/` | Stop local MongoDB container |
| `node app_api/models/seed.js` | `travlr/` | Seed initial database trip records |
| `npm start` | `travlr/` | Start Express backend server on port 3000 |
| `npm run build` | `travlr/app_admin/` | Build Angular SPA distribution bundle |
| `ng serve` | `travlr/app_admin/` | Start Angular SPA dev server on port 4200 |
