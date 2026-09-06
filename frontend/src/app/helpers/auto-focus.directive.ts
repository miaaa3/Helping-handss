import { AfterViewInit, Directive, ElementRef } from '@angular/core';

/**
 * Focuses the host element as soon as it's added to the DOM.
 * Handy on inputs/textareas revealed via *ngIf (e.g. an expanding comment box),
 * since Angular re-creates the element each time the condition becomes true.
 */
@Directive({
  selector: '[appAutoFocus]'
})
export class AutoFocusDirective implements AfterViewInit {
  constructor(private elementRef: ElementRef<HTMLElement>) {}

  ngAfterViewInit(): void {
    setTimeout(() => this.elementRef.nativeElement.focus());
  }
}
