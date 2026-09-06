# HelpingHands Backend

Spring Boot backend for HelpingHands, a volunteering platform connecting volunteers and organizations through opportunities, applications, social posts, messaging, donations, notifications, and admin moderation.

## Current Product Shape

The backend supports the main platform workflows:

- Volunteer, organization, and admin users
- JWT authentication and role-based access
- Profiles, search, follows, posts, media uploads, likes, and comments
- Opportunities with categories, dates, locations, capacity, and statuses
- Opportunity applications with pending, accepted, rejected, and cancelled states
- Volunteer and organization dashboard data
- Admin moderation, organization verification, and platform management endpoints
- Notifications with unread counts, pagination, mark-read, and mark-all-read
- Real-time private messages over STOMP/SockJS WebSocket
- Stripe donation intents, webhook processing, donation history, organization totals, and mixed payment statuses
- Rich local sample data for realistic UI testing

The API is designed for the Angular frontend in the companion `HelpingHands` repository.

## Tech Stack

- Java 17
- Spring Boot 3.1.3
- Spring Security
- Spring Data JPA
- MySQL
- JWT authentication with `java-jwt`
- Spring WebSocket/STOMP
- Stripe Java SDK
- Lombok
- Maven wrapper

## Project Structure

```text
src/main/java/com/example/HelpingHands/
  AuthenticationRequestsAndResponses/  Auth request and response DTOs
  Configuration/                       Security, CORS, WebSocket, Stripe, MVC, sample data
  Controller/                          REST and STOMP API entry points
  DTO/                                 API response/request DTOs
  Entity/                              JPA entities
  Exception/                           Custom exceptions
  Listener/                            Entity timestamp listener
  Repository/                          Spring Data repositories
  Service/                             Service interfaces
  ServiceImpl/                         Service implementations
```

## Requirements

- Java 17
- MySQL
- Maven wrapper from this repository
- Stripe test keys for donation testing

## Local Setup

Create a MySQL database:

```sql
CREATE DATABASE HelpingHands;
```

Recommended local variables:

```bash
export JWT_SECRET="change-this-local-secret"
export DB_URL="jdbc:mysql://localhost:8889/HelpingHands"
export DB_USERNAME="root"
export DB_PASSWORD="root"
export UPLOAD_DIR="./uploads"
export CORS_ALLOWED_ORIGINS="http://localhost:4200"
export STRIPE_SECRET_KEY="sk_test_your_key"
export STRIPE_PUBLISHABLE_KEY="pk_test_your_key"
export STRIPE_WEBHOOK_SECRET="whsec_your_secret"
```

Run the backend:

```bash
./mvnw spring-boot:run
```

The API runs on `http://localhost:8080/` by default.

## Sample Data

Local sample data is enabled by default:

```properties
sample-data.enabled=${SAMPLE_DATA_ENABLED:true}
```

The seeder is idempotent and creates a realistic demo world with:

- admin account
- multiple volunteers and organizations
- verified, pending, and rejected organizations
- posts, comments, likes, follows
- open, full, closed, and draft opportunities
- pending, accepted, rejected, and cancelled applications
- messages
- donations with succeeded, pending, failed, and refunded statuses
- notifications

Demo password for seeded users:

```text
Password123!
```

Useful demo accounts:

```text
admin@helpinghands.test
maya.volunteer@helpinghands.test
theo.volunteer@helpinghands.test
foodbridge.org@helpinghands.test
greensteps.org@helpinghands.test
```

To disable seeding:

```bash
export SAMPLE_DATA_ENABLED=false
```

## Default Local Ports

- Backend API: `8080`
- MySQL with MAMP default: `8889`
- Frontend dev server: `4200`

## Configuration

Configuration lives in:

```text
src/main/resources/application.properties
```

Environment-backed settings:

- `JWT_SECRET`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `UPLOAD_DIR`
- `CORS_ALLOWED_ORIGINS`
- `STRIPE_SECRET_KEY`
- `STRIPE_PUBLISHABLE_KEY`
- `STRIPE_WEBHOOK_SECRET`
- `SAMPLE_DATA_ENABLED`

## API Overview

### Authentication

- `POST /auth/register/volunteer`
- `POST /auth/register/organization`
- `POST /auth/login`
- `POST /auth/logout`

### Users And Profiles

- `GET /api/users/getUser`
- `GET /api/users/search?keyword=...`
- `PUT /api/users/updateVolunteer`

### Posts And Media

- `POST /api/posts/createPost`
- `GET /api/posts/getAllPosts`
- `GET /api/posts/getPost?id=...`
- `GET /api/posts/getMediaForPost?postId=...`
- `PUT /api/posts/updatePost?id=...`
- `DELETE /api/posts/deletePost/{postId}`
- `GET /uploads/{fileName}`

### Likes, Comments, And Follows

- `POST /api/likes/createLike?postId=...`
- `DELETE /api/likes/deleteLike?likeId=...`
- `POST /api/comments/createComment?postId=...&content=...`
- `GET /api/comments/getCommentsByPostId?postId=...`
- `PUT /api/comments/updateComment`
- `DELETE /api/comments/deleteComment?commentId=...`
- `POST /api/follow/follow?userId=...`
- `GET /api/follow/getFollowers`
- `GET /api/follow/getFollowing`
- `DELETE /api/follow/unfollow?userId=...`

### Opportunities And Applications

Exact controller paths should be checked before wiring new frontend calls, but this backend includes services/controllers for:

- creating, editing, listing, and deleting opportunities
- filtering opportunities by status/category/organization
- applying to opportunities
- cancelling volunteer applications
- accepting/rejecting applicants from organization dashboards
- dashboard summaries for volunteers and organizations

### Notifications

- paginated notification list for the current user
- unread count for navbar/bell badges
- mark one notification as read
- mark all notifications as read

### Messages

- `GET /api/messages/conversations`
- `GET /api/messages/conversation/{userId}`
- `PUT /api/messages/conversation/{userId}/read`
- WebSocket endpoint: `/ws`
- STOMP send destination: `/app/chat.send`
- STOMP user receive destination: `/user/queue/messages`

### Donations

- `POST /api/donations/create-intent`
- `POST /api/donations/webhook`
- `GET /api/donations/organization/{organizationId}`
- `GET /api/donations/organization/{organizationId}/total`
- `GET /api/donations/my-donations`
- `GET /api/donations/config`

### Admin

Admin endpoints support platform operations such as:

- reviewing organization verification
- managing users
- managing opportunities
- moderation/admin dashboard data

Check controller files for exact paths before adding or changing frontend calls.

## Security Notes

- `/auth/**`, `/uploads/**`, `/ws/**`, `/api/donations/webhook`, and `/api/donations/config` are publicly reachable.
- Most `/api/**` endpoints require authentication.
- Role-specific actions should remain protected server-side, not only in the frontend.
- CORS is controlled through `CORS_ALLOWED_ORIGINS`.
- Do not commit real JWT secrets, database passwords, or Stripe live keys.

## Testing

```bash
./mvnw test
./mvnw -DskipTests compile
```

Current automated tests are limited. Important flows should be manually verified while backend integration tests are added.

## Best Next Work

- Finish admin/moderation flows end to end.
- Add report entities/endpoints for posts, comments, users, organizations, opportunities, and messages.
- Add validation annotations and `@Valid` to request DTO/controller methods.
- Add integration tests for auth, opportunities, applications, notifications, messages, and donations.
- Add pagination consistently for feeds, comments, conversations, users, opportunities, notifications, and donation lists.
- Replace direct entity responses with DTOs consistently.
- Add a dedicated test profile using H2 or Testcontainers so `./mvnw test` works without local MySQL.
