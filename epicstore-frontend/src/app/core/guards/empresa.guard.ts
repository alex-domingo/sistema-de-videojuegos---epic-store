import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { SessionState } from '../state/session.state';
import { Router } from '@angular/router';

export const empresaGuard: CanActivateFn = () => {
    const state = inject(SessionState);
    const router = inject(Router);

    if (state.loading()) return true;

    if (!state.autenticado()) {
        router.navigateByUrl('/login-empresa');
        return false;
    }

    if (!state.esEmpresa()) {
        router.navigateByUrl('/forbidden');
        return false;
    }

    return true;
};
