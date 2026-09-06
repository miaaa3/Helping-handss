/** Mirrors Spring Data's Page<T> JSON shape, for paginated endpoints. */
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;   // current page index (0-based)
  size: number;
  last: boolean;
  first: boolean;
  empty: boolean;
}
