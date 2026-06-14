# HelpingHands Frontend

HelpingHands is an Angular frontend for a volunteering platform where volunteers and organizations can discover opportunities, apply, communicate, donate, and track activity from role-based dashboards.

The Angular app lives in `FrontApp/` and talks to the Spring Boot backend in the companion `HelpingHands-backend` repository.

## Current Product Shape

The app is no longer just a social feed. It now has the main platform surfaces:

- Volunteer and organization registration/login
- Authenticated layout with navbar/sidebar navigation
- Social feed with posts, media, likes, comments, follows, and profiles
- Opportunity discovery and role-specific opportunity/dashboard flows
- Applications with statuses such as pending, accepted, rejected, and cancelled
- Organization/admin moderation and verification work in progress
- Real-time private messaging through STOMP over SockJS/WebSocket
- Donations, donation history, and Stripe client integration
- Notification UI hooks for unread activity and backend notification endpoints
- Local seeded demo data from the backend for realistic testing

## Project Structure

```text
FrontApp/
  src/app/
    admin-dashboard/       Admin moderation and platform management UI
    donation/              Donation dialog and donation history views
    guard/                 Auth/admin route guards
    helpers/               HTTP auth interceptor helper code
    home/                  Authenticated feed/dashboard surface
    login/                 Login page
    main-navbar/           Top navigation
    main-sidebar/          Sidebar navigation
    messages/              Conversation list and chat window
    models/                TypeScript interfaces and DTO models
    post/                  Feed post UI and interactions
    services/              API, auth, chat, donation, admin, post, user services
    settings/              Profile/settings update page
    user-profile/          User profile view
    welcome-page/          Public welcome page
  src/environments/
    environment.ts         Local API base URL
```

## Requirements

- Node.js 18 LTS recommended
- npm
- Angular CLI 16, or run Angular commands through `npx ng`
- HelpingHands backend running on `http://localhost:8080/`

## Setup

```bash
cd FrontApp
npm install
npm start
```

Open `http://localhost:4200/`.

## Useful Commands

```bash
npm start
npm run build
npm test
```

## Configuration

The API URL is configured in:

```text
FrontApp/src/environments/environment.ts
```

Default local API:

```ts
apiUrl: 'http://localhost:8080/'
```

Production uses `environment.prod.ts`; update it before deployment if the backend is not same-origin.

## Backend Capabilities Expected

The frontend expects the backend to provide:

- Auth: register volunteer, register organization, login/logout
- Users/profiles/search/follow status
- Posts/media/likes/comments
- Follows/unfollows
- Opportunities and application actions
- Role dashboards for volunteers and organizations
- Admin actions for users, organizations, opportunities, moderation, and verification
- Notifications with unread counts, pagination, mark-read, and mark-all-read
- Messages via REST history plus WebSocket delivery
- Donations via Stripe payment intents, config, history, totals, and webhook-backed statuses
- Uploaded media from `/uploads/{fileName}`

## Current Notes

- This is a classic Angular module app, not standalone components.
- JWT is stored client-side and attached to protected API calls through the interceptor.
- Media URLs are built from the backend `/uploads/` static route.
- Chat uses SockJS/STOMP and reconnects automatically.
- Stripe payment confirmation depends on backend Stripe keys and webhook setup.
- The backend seeder now creates a realistic demo world, so use those seeded accounts to test full UI states.

## Best Next Work

Focus on making the app feel finished and trustworthy:

- Finish admin dashboard screens and connect every admin action cleanly.
- Wire notifications fully to the backend notification API.
- Polish small UX details: logo navigation, active nav links, comment expansion/focus, empty states, loading states, disabled saving buttons, and success/error toasts.
- Add an opportunity details page with apply/cancel state, spots left, org info, and related opportunities.
- Add report buttons for posts, comments, users, organizations, opportunities, and messages.
- Improve profile trust UI with verified badges, active opportunities, impact, and donation/volunteer history.
- Add search/filter UX for opportunities by category, city/remote, date, status, and verified organization.

## Demo Login Hint

The backend sample data uses `Password123!` for local demo accounts such as:

- `admin@helpinghands.test`
- `maya.volunteer@helpinghands.test`
- `foodbridge.org@helpinghands.test`
