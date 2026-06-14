import { Pipe, PipeTransform } from '@angular/core';

/**
 * Formats a date as a short relative time string (IG/FB style): "now", "5m",
 * "2h", "3d", "4w", or falls back to a short date once it's over a year old.
 */
@Pipe({
  name: 'timeAgo',
  pure: false // re-evaluates on each change-detection cycle so times keep ticking forward
})
export class TimeAgoPipe implements PipeTransform {
  transform(value?: string | Date | null): string {
    if (!value) return '';

    const date = value instanceof Date ? value : new Date(value);
    if (isNaN(date.getTime())) return '';

    const seconds = Math.floor((Date.now() - date.getTime()) / 1000);
    if (seconds < 5) return 'now';
    if (seconds < 60) return `${seconds}s`;

    const minutes = Math.floor(seconds / 60);
    if (minutes < 60) return `${minutes}m`;

    const hours = Math.floor(minutes / 60);
    if (hours < 24) return `${hours}h`;

    const days = Math.floor(hours / 24);
    if (days < 7) return `${days}d`;

    const weeks = Math.floor(days / 7);
    if (weeks < 4) return `${weeks}w`;

    const months = Math.floor(days / 30);
    if (months < 12) return `${months}mo`;

    const years = Math.floor(days / 365);
    return `${years}y`;
  }
}
