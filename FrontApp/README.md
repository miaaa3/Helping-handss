# HelpingHands Angular App

Angular 16 frontend for the HelpingHands volunteering social network.

## Summary

This app provides the browser experience for volunteers and organizations. Users can register, log in, browse a protected social feed, create posts with media, like and comment on posts, follow other users, update profile details, send real-time private messages, and make or review donations.

The app uses Angular, Angular Material, Tailwind CSS, RxJS, SockJS/STOMP for chat, Stripe.js for donations, and an HTTP interceptor for JWT-authenticated backend calls.

## Run Locally

```bash
npm install
npm start
```

Then open `http://localhost:4200/`.

The backend should be running at `http://localhost:8080/` unless `src/environments/environment.ts` is changed.

## Scripts

```bash
npm start       # Run Angular dev server
npm run build   # Build production bundle
npm test        # Run Karma/Jasmine tests
```

## Important Folders

```text
src/app/services/      API services for auth, posts, users, likes, comments, follows, chat, and donations
src/app/models/        TypeScript models used by the API layer and components
src/app/messages/      Conversation list and chat UI
src/app/donation/      Donate dialog and donation history UI
src/app/post/          Feed post component
src/app/guard/         AuthGuard for protected routes
src/environments/      API URL configuration
```

## Main Routes

- `/welcome-page` public welcome page
- `/login` login
- `/sign-up-volunteer` volunteer registration
- `/sign-up-organization` organization registration
- `/home` protected feed
- `/settings` protected profile/settings page
- `/user-profile` protected profile page
- `/messages` protected messaging page
- `/my-donations` protected donation history

## Configuration

Edit `src/environments/environment.ts` to change the backend URL:

```ts
export const environment = {
  apiUrl: 'http://localhost:8080/',
  enableDebug: false
};
```

## Notes For Development

- Keep frontend DTOs in `src/app/models/` aligned with backend DTOs.
- Protected API calls rely on the token storage service and HTTP interceptor.
- Media files are loaded from the backend `/uploads/` route.
- Chat connects to the backend `/ws` endpoint and sends messages to `/app/chat.send`.
- Donation UI depends on Stripe publishable key config from `/api/donations/config`.
