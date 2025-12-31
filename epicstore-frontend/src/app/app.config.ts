import { ApplicationConfig, APP_INITIALIZER, inject } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { routes } from './app.routes';
import { AuthService } from './core/services/auth.service';
import { SessionState } from './core/state/session.state';

function initSession() {
  return () => {
    const auth = inject(AuthService);
    const state = inject(SessionState);

    return new Promise<void>((resolve) => {
      auth.me().subscribe({
        next: (resp) => state.session.set(resp.datos),
        error: () => state.session.set({ autenticado: false }),
        complete: () => {
          state.loading.set(false);
          resolve();
        }
      });
    });
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    { provide: APP_INITIALIZER, useFactory: initSession, multi: true }
  ]
};
