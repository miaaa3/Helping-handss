# SUMMARY_FRONT

Local AI handoff summary for the HelpingHands frontend. This file is intentionally ignored by Git.

## Repo

- Path: `/Users/miaa/PROJECTS/01-Web-and-App/Helping-hands/HelpingHands`
- Main app folder: `FrontApp/`
- Framework: Angular 16
- Default frontend URL: `http://localhost:4200/`
- Default backend URL: `http://localhost:8080/`
- API URL config: `FrontApp/src/environments/environment.ts`

## Current Purpose

HelpingHands frontend is the browser client for a volunteering platform. It supports auth, social feed, profiles, follows, comments, likes, opportunities, applications, dashboards, messages, donations, notifications, and admin/moderation work.

## Important Frontend Areas

- `FrontApp/src/app/app-routing.module.ts`: route table, auth/admin route protection, and page registration.
- `FrontApp/src/app/app.module.ts`: Angular module declarations/imports.
- `FrontApp/src/app/admin-dashboard/`: admin platform management UI.
- `FrontApp/src/app/main-navbar/` and `FrontApp/src/app/main-sidebar/`: primary navigation.
- `FrontApp/src/app/home/`: feed/dashboard entry surface.
- `FrontApp/src/app/post/`: post card, comments, likes, and media UI.
- `FrontApp/src/app/messages/`: REST conversation history plus WebSocket chat UI.
- `FrontApp/src/app/donation/`: donation dialog and donation history.
- `FrontApp/src/app/services/`: API services for auth, users, posts, comments, likes, follows, chat, donations, admin, token storage, and HTTP interception.
- `FrontApp/src/app/models/`: TypeScript DTOs and API models.
- `FrontApp/src/app/guard/`: auth/admin route guards.

## Backend Assumptions

Frontend expects backend support for:

- Auth: `/auth/register/volunteer`, `/auth/register/organization`, `/auth/login`
- Users/search/profiles/follows
- Posts, uploaded media, likes, comments
- Opportunities and applications
- Volunteer/organization dashboards
- Admin organization verification, user management, opportunity management, and moderation endpoints
- Notifications: paginated list, unread count, mark one read, mark all read
- Messages: `/api/messages/conversations`, `/api/messages/conversation/{userId}`, `/ws`
- Donations: create intent, config, donor history, organization totals/statuses
- Media: `/uploads/{fileName}`

## Current Git Notes

As of the latest check, the frontend repo has uncommitted non-doc changes in admin/dashboard-related files and shared app files:

- `FrontApp/src/app/app-routing.module.ts`
- `FrontApp/src/app/app.module.ts`
- `FrontApp/src/app/main-sidebar/*`
- `FrontApp/src/app/models/user.ts`
- `FrontApp/src/styles.css`
- new admin guard/model/service/dashboard files

Do not overwrite those without reading them first.

## Useful Commands

```bash
cd FrontApp
npm install
npm start
npm run build
npm test
```

## Good Next Tasks

- Finish and verify the admin dashboard UI.
- Connect notification UI to the backend notification endpoints.
- Polish small UX details: logo navigation, active nav, comments expansion/focus, empty states, loading states, disabled submit buttons, and toasts.
- Add opportunity details page and stronger opportunity filters.
- Add report actions for posts/comments/users/orgs/opportunities/messages.
- Run `npm run build` after frontend changes settle.
