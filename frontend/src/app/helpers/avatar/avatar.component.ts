import { Component, Input } from '@angular/core';

/** Profile picture values that mean "no real picture set" - render initials instead. */
const PLACEHOLDER_AVATARS = new Set<string>([
  'assets/images/default-avatar.png',
  'assets/default-profile.png',
  'https://avatars.githubusercontent.com/u/67946056?v=4',
]);

/** Background colors cycled through for initials avatars, keyed by name hash. */
const AVATAR_COLORS = [
  '#16a34a', '#2563eb', '#dc2626', '#d97706',
  '#9333ea', '#0d9488', '#db2777', '#4f46e5',
];

/**
 * Displays a user's profile picture, or - if none is set (or only the
 * old placeholder default) - a colored circle with their initials,
 * Instagram/Slack-style.
 */
@Component({
  selector: 'app-avatar',
  templateUrl: './avatar.component.html',
})
export class AvatarComponent {
  /** Profile picture URL. Empty or placeholder values trigger the initials fallback. */
  @Input() src?: string | null;

  /** Full name used to derive initials and a consistent background color. */
  @Input() name = '';

  /** Tailwind size/shape/spacing classes applied to either the image or the initials circle. */
  @Input() sizeClass = 'w-10 h-10 rounded-full object-cover flex-shrink-0';

  get hasImage(): boolean {
    return !!this.src && !PLACEHOLDER_AVATARS.has(this.src);
  }

  get initials(): string {
    const parts = (this.name || '').trim().split(/\s+/).filter(Boolean);
    if (!parts.length) {
      return '?';
    }
    const first = parts[0].charAt(0);
    const last = parts.length > 1 ? parts[parts.length - 1].charAt(0) : '';
    return (first + last).toUpperCase();
  }

  get bgColor(): string {
    let hash = 0;
    const n = this.name || '';
    for (let i = 0; i < n.length; i++) {
      hash = (hash * 31 + n.charCodeAt(i)) >>> 0;
    }
    return AVATAR_COLORS[hash % AVATAR_COLORS.length];
  }

  /** Scales the initials' font size proportionally to the avatar's width (w-N Tailwind class). */
  get fontSize(): string {
    const match = this.sizeClass.match(/w-(\d+)/);
    const units = match ? parseInt(match[1], 10) : 10;
    return `${(units * 0.25 * 0.42).toFixed(2)}rem`;
  }
}
