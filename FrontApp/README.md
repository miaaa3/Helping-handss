# HelpingHands Angular App

Angular 16 client for HelpingHands, a volunteer and organization platform with social feed, opportunities, applications, dashboards, messaging, donations, notifications, and admin moderation surfaces.

## Run Locally

```bash
npm install
npm start
```

Open `http://localhost:4200/`.

The backend should be running at `http://localhost:8080/` unless `src/environments/environment.ts` is changed.

## Scripts

```bash
npm start
npm run build
npm test
```

## Important Folders

```text
src/app/admin-dashboard/   Admin platform management UI
src/app/donation/          Donate dialog and donation history UI
src/app/guard/             Auth/admin route guards
src/app/messages/          Conversation list and chat UI
src/app/models/            TypeScript API models
src/app/post/              Feed post component
src/app/services/          API services for auth, users, posts, admin, chat, donations, etc.
src/environments/          API URL configuration
```

## Main Routes

- `/welcome-page`
- `/login`
- `/sign-up-volunteer`
- `/sign-up-organization`
- `/home`
- `/dashboard`
- `/settings`
- `/user-profile`
- `/messages`
- `/my-donations`
- Admin routes under the admin dashboard flow

## Development Notes

- Keep `src/app/models/` aligned with backend DTOs.
- Protected API calls rely on token storage plus the HTTP interceptor.
- Media loads from backend `/uploads/`.
- Chat connects to `/ws` and sends through `/app/chat.send`.
- Donation UI depends on `/api/donations/config`.
- Local demo data from the backend uses `Password123!` for seeded users.

## Current Priorities

- Finish admin dashboard wiring.
- Use the backend notification API fully.
- Polish small UX details like logo navigation, active nav links, comments UX, loading/empty states, and button disabled states.
- Add opportunity details, report flows, trust badges, and better opportunity filters.
