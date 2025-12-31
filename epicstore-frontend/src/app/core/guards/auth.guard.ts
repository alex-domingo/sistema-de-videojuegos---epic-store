import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { SessionState } from '../state/session.state';
import { Router } from '@angular/router';

export const authGuard: CanActivateFn = () => {
    const state = inject(SessionState);
    const router = inject(Router);

    // si aún está cargando, dejamos pasar (APP_INITIALIZER ya lo resolvió casi siempre)
    if (state.loading()) return true;

    if (!state.autenticado()) {
        router.navigateByUrl('/login');
        return false;
    }
    return true;
};
