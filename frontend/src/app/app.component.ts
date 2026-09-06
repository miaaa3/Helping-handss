import { Component } from '@angular/core';
import { trigger, transition, style, animate } from '@angular/animations';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    // Subtle fade when the routed page changes. Animates the host wrapper's
    // opacity only (no enter/leave queries) so it's cheap and never causes
    // two pages to overlap.
    trigger('routeFade', [
      transition('* => *', [
        style({ opacity: 0 }),
        animate('180ms ease-out', style({ opacity: 1 }))
      ])
    ])
  ]
})
export class AppComponent {
  title = 'FrontApp';

  /** Returns a key that changes whenever the activated route changes, used to trigger the fade. */
  getRouteKey(outlet: RouterOutlet): string {
    return outlet?.isActivated ? (outlet.activatedRoute.routeConfig?.path ?? '') : '';
  }
}
