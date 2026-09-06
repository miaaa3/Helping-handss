# HelpingHands — Project Guide

Everything you need to run, test, and develop the project.

---

## What is this?

A volunteering platform connecting volunteers and organizations. Volunteers discover opportunities, apply, follow orgs, chat, and donate. Organizations post opportunities, manage applicants, and build a following. Admins moderate the platform.

**Stack:** Spring Boot 3.1.3 (Java 17) + Angular 16 + MySQL

---

## Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| Java | 17 | `java -version` |
| Maven | via `./mvnw` | wrapper included, no install needed |
| Node.js | 18 LTS | `node -v` |
| npm | bundled with Node | |
| MySQL | any recent | MAMP default: port 8889, user `root`, pass `root` |
| Angular CLI | 16 | `npm i -g @angular/cli@16` or use `npx ng` |

---

## Running the project

### 1. Backend

```bash
cd backend/

# First run — ensure the DB exists in MySQL:
# CREATE DATABASE HelpingHands;

./mvnw clean spring-boot:run
```

Runs on **http://localhost:8080**

Default DB config (MAMP): `jdbc:mysql://localhost:8889/HelpingHands`, user `root`, pass `root`

Override with env vars if your MySQL setup differs:
```bash
DB_URL=jdbc:mysql://localhost:3306/HelpingHands \
DB_USERNAME=root \
DB_PASSWORD=yourpassword \
./mvnw spring-boot:run
```

Schema is auto-applied (`ddl-auto=update`) — no migration scripts needed.

Sample data seeds automatically on first run (`sample-data.enabled=true`).

### 2. Frontend

```bash
cd frontend/

npm install
npm start          # or: npx ng serve
```

Runs on **http://localhost:4200**

---

## Test accounts

All accounts share the same password: **`Password123!`**

### Admin
| Email | Password | Role |
|-------|----------|------|
| admin@helpinghands.test | Password123! | Admin |

### Volunteers
| Email | Name |
|-------|------|
| maya.volunteer@helpinghands.test | Maya Carter |
| theo.volunteer@helpinghands.test | Theo Martin |
| lina.volunteer@helpinghands.test | Lina Haddad |
| sofia.volunteer@helpinghands.test | Sofia Nguyen |
| adam.volunteer@helpinghands.test | Adam Williams |
| ines.volunteer@helpinghands.test | Ines Moreau |
| noah.volunteer@helpinghands.test | Noah Garcia |
| camille.volunteer@helpinghands.test | Camille Robert |

### Organizations
| Email | Display Name |
|-------|-------------|
| foodbridge.org@helpinghands.test | FoodBridge Paris |
| greensteps.org@helpinghands.test | GreenSteps |
| brightfuture.org@helpinghands.test | Bright Future Tutors |
| pawscare.org@helpinghands.test | PawsCare Shelter |
| reliefnow.org@helpinghands.test | ReliefNow |
| seniorcircle.org@helpinghands.test | Senior Circle |

> All sample data is seeded idempotently — re-running the backend won't duplicate it.

---

## Project structure

```
Helping-hands/
├── backend/          Spring Boot API
│   └── src/main/java/com/example/HelpingHands/
│       ├── Configuration/         Security, CORS, WebSocket, Stripe, sample data seeder
│       ├── Controller/            REST + STOMP endpoints
│       ├── DTO/                   API request/response shapes
│       ├── Entity/                JPA entities (UserEntity, Volunteer, Organization, …)
│       ├── Repository/            Spring Data repositories
│       ├── Service/ + ServiceImpl/ Business logic
│       └── Exception/             Custom exceptions
│
└── frontend/         Angular 16 app
    └── src/app/
        ├── home/                  Feed + sidebar
        ├── user-profile/          Profile page (Facebook-style)
        ├── main-navbar/           Top nav with search + notifications
        ├── welcome-page/          Public landing page
        ├── messages/              DM chat (STOMP/WebSocket)
        ├── notifications/         Notification list + follow requests
        ├── admin-dashboard/       Admin moderation UI
        ├── donation/              Stripe donation flow
        ├── services/              API service layer
        └── models/                TypeScript interfaces
```

---

## Key API endpoints

### Auth
```
POST /auth/register/volunteer
POST /auth/register/organization
POST /auth/login
```

### Users & Search
```
GET  /api/users/getUser              current user profile
GET  /api/users/search?keyword=      search users
GET  /api/users/suggestions          suggested users to follow
GET  /api/users/getUser/{id}         any user's public profile
PUT  /api/users/updateVolunteer
```

### Follow system
```
POST /api/follow/follow?userId=      follow / unfollow / send request
GET  /api/follow/getFollowers
GET  /api/follow/getFollowing
GET  /api/follow/requests/pending    incoming follow requests
POST /api/follow/requests/{id}/accept
POST /api/follow/requests/{id}/reject
```

### Posts
```
POST /api/posts/createPost
GET  /api/posts/getAllPosts
DELETE /api/posts/deletePost/{id}
POST /api/likes/createLike?postId=
POST /api/comments/createComment?postId=&content=
```

### Opportunities
```
GET  /public/opportunities           public list (no auth)
GET  /api/opportunities/feed         feed for current user (or all if admin)
POST /api/opportunities/create
PUT  /api/opportunities/update/{id}
DELETE /api/opportunities/delete/{id}
POST /api/applications/apply/{opportunityId}
GET  /api/applications/my
```

### Messaging (WebSocket)
```
GET  /api/messages/conversations
GET  /api/messages/conversation/{userId}
WS   /ws  →  /app/chat.send         send message
WS         →  /user/queue/messages  receive
```

### Notifications
```
GET  /api/notifications?page=0&size=5
GET  /api/notifications/unread-count
PUT  /api/notifications/{id}/read
PUT  /api/notifications/read-all
```

### Donations (Stripe test mode)
```
GET  /api/donations/config           get publishable key
POST /api/donations/create-intent
POST /api/donations/webhook
GET  /api/donations/my-donations
```

### Admin
```
GET  /api/admin/users
GET  /api/admin/opportunities
POST /api/admin/verify-organization/{id}
```

---

## Follow request flow

New follows start as **PENDING** (Instagram-style). The target user sees requests in `/notifications` and can accept or decline. Once accepted the follow becomes **ACCEPTED** and both users see each other in their followers/following lists. Existing data defaults to ACCEPTED.

States: `NONE → PENDING → ACCEPTED` (or rejected/cancelled back to NONE)

---

## Stripe (donations)

The backend ships with placeholder Stripe keys. To test donations:

1. Get test keys from https://dashboard.stripe.com/test/apikeys
2. Set env vars when starting the backend:
```bash
STRIPE_SECRET_KEY=sk_test_... \
STRIPE_PUBLISHABLE_KEY=pk_test_... \
./mvnw spring-boot:run
```
Use Stripe test card `4242 4242 4242 4242`, any future expiry, any CVC.

---

## Common issues

| Problem | Fix |
|---------|-----|
| 100 Lombok compile errors | Add `annotationProcessorPaths` to `maven-compiler-plugin` in `pom.xml` (already done) |
| DB connection refused | Check MySQL is running; verify port (MAMP = 8889, standard = 3306) |
| `CREATE DATABASE` error | Create `HelpingHands` DB manually before first run |
| Frontend 401 on all requests | Token expired — log out and log back in |
| Admin feed empty | Admin sees all public opportunities (no follow required) |
| Follower count shows -1 | Fixed — was an off-by-one bug in UserController |
| CORS error | Make sure backend `CORS_ALLOWED_ORIGINS` includes `http://localhost:4200` |

---

## Environment variables (backend)

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:mysql://localhost:8889/HelpingHands` | JDBC URL |
| `DB_USERNAME` | `root` | DB user |
| `DB_PASSWORD` | `root` | DB password |
| `JWT_SECRET` | (long default string) | Change in production |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:4200` | Frontend origin |
| `UPLOAD_DIR` | `./uploads` | Media upload folder |
| `SAMPLE_DATA_ENABLED` | `true` | Seed demo accounts |
| `STRIPE_SECRET_KEY` | `sk_test_placeholder` | Stripe secret key |
| `STRIPE_PUBLISHABLE_KEY` | `pk_test_placeholder` | Stripe publishable key |
| `STRIPE_WEBHOOK_SECRET` | `whsec_placeholder` | Stripe webhook secret |

---

## Build for production

```bash
# Backend JAR
cd backend/
./mvnw clean package -DskipTests
java -jar target/HelpingHands-*.jar

# Frontend dist
cd frontend/
npx ng build --configuration production
# Output in dist/ — serve with nginx or any static host
```
