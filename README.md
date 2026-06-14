# HelpingHands Frontend

HelpingHands is a volunteering social network frontend built with Angular. It connects volunteers and organizations through user profiles, a social feed, follows, notifications, real-time messages, and donation flows.

This repository contains the Angular client in `FrontApp/`.

## Project Summary

The frontend is the user-facing part of the HelpingHands platform. It lets users register as volunteers or organizations, sign in with JWT-based authentication, browse a protected home feed, create posts with optional media uploads, interact through likes and comments, follow other users, update their profile, chat in real time, and view or create donations.

The app talks to the Spring Boot backend at `http://localhost:8080/` by default. Protected API calls use an HTTP interceptor to attach the saved authentication token.

## Main Features

- Volunteer and organization registration
- Login and token-based authenticated sessions
- Protected routes for home, settings, profile, messages, and donation history
- Social feed with post creation, media upload, likes, comments, and delete support
- User search and follow/unfollow behavior
- Notification display through the logged-in user payload
- Volunteer profile updates through settings
- Real-time private messaging using STOMP over SockJS/WebSocket
- Stripe donation intent flow and "My Donations" page
- Angular Material, Tailwind, and custom CSS styling

## Project Structure

```text
FrontApp/
  src/app/
    donation/              Donation dialog and donation history views
    guard/                 Route authentication guard
    helpers/               Auth interceptor helper code
    home/                  Main protected feed page
    login/                 Login page
    main-navbar/           Authenticated top navigation
    main-sidebar/          Authenticated sidebar navigation
    messages/              Conversation list and chat window
    models/                TypeScript interfaces and DTO models
    organization-registration-page/
    post/                  Feed post UI and interactions
    services/              API, auth, chat, donation, post, user services
    settings/              Profile/settings update page
    user-profile/          User profile view
    volunteer-registration-page/
    welcome-page/          Public landing/welcome page
  src/environments/
    environment.ts         API base URL and frontend environment flags
```

## Requirements

- Node.js 18.13+ (Angular 16 supports Node 16.14+ / 18.10+; 18 LTS recommended)
- npm
- Angular CLI 16 (`npm install -g @angular/cli@16`, or use `npx ng`)
- Running HelpingHands backend on port `8080`

## Setup

```bash
cd FrontApp
npm install
npm start
```

Open `http://localhost:4200/`.

## Default Local Ports

- Frontend dev server: `4200`
- Backend API (companion repo): `8080`

## Configuration

The API base URL lives in `FrontApp/src/environments/`:

- `environment.ts` - used for `ng serve` / development builds. Default: `apiUrl: 'http://localhost:8080/'`
- `environment.prod.ts` - swapped in automatically for `ng build --configuration=production` via `fileReplacements` in `angular.json`. Default: `apiUrl: '/'` (same-origin; update to your deployed backend's base URL)

Change `environment.ts` when pointing local development at a different backend port, and `environment.prod.ts` before deploying.

## Useful Commands

```bash
npm start
npm run build
npm test
```

## Backend Connection

The frontend expects these backend capabilities:

- `POST /auth/register/volunteer`
- `POST /auth/register/organization`
- `POST /auth/login`
- `GET /api/users/getUser`
- `GET /api/users/search`
- `PUT /api/users/updateVolunteer`
- `GET /api/posts/getAllPosts`
- `POST /api/posts/createPost`
- `DELETE /api/posts/deletePost/{postId}`
- `POST /api/likes/createLike`
- `POST /api/comments/createComment`
- `POST /api/follow/follow`
- `GET /api/messages/conversations`
- `GET /api/messages/conversation/{userId}`
- `POST /api/donations/create-intent`
- `GET /api/donations/my-donations`
- `GET /api/donations/config`
- `PUT /api/messages/conversation/{userId}/read`
- WebSocket endpoint `/ws` with STOMP destination `/app/chat.send`

## Current Notes

- The frontend is currently organized as a classic Angular module app rather than standalone components.
- Media URLs are built from the backend `/uploads/` static route.
- Chat uses SockJS and STOMP with JWT passed during the STOMP connection. The client auto-reconnects
  (`reconnectDelay: 5000`), re-syncs the open conversation and sidebar list after a reconnect, marks
  messages as read as they're viewed, and dedupes incoming messages by id.
- All HTTP errors are surfaced via a global interceptor (`HttpInterceptorService`) as toast
  notifications (expired session, validation errors, payment failures, server unavailable, etc.).
- Stripe payment confirmation depends on the backend donation and webhook configuration.

## Suggested Next Improvements

- Add stronger loading and empty states across feed, messages, and donations.
- Expand test coverage for auth, route guards, post creation, chat, and donation flows.
- Add a shared API contract or generated client to keep frontend and backend DTOs in sync.
