/**
 * `sockjs-client` (used by ChatService for the STOMP/WebSocket chat connection)
 * expects the Node.js `global` object, which Webpack 5 no longer polyfills
 * automatically. Without this, the vendor bundle throws
 * "ReferenceError: global is not defined" at startup and the whole app
 * fails to bootstrap (blank pages everywhere).
 */
(window as any).global = window;
