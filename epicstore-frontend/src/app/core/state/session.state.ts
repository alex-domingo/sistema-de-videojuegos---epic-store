import { Injectable, signal, computed } from '@angular/core';
import type { AuthMeResponse } from '../models/auth.model';

type SessionDatos = AuthMeResponse['datos'];

function isAutenticado(s: SessionDatos | null): s is Exclude<SessionDatos, { autenticado: false }> {
    return !!s && s.autenticado === true;
}

@Injectable({ providedIn: 'root' })
export class SessionState {
    session = signal<SessionDatos | null>(null);
    loading = signal(true);

    autenticado = computed(() => isAutenticado(this.session()));

    tipoSesion = computed(() => {
        const s = this.session();
        return isAutenticado(s) ? s.tipoSesion : null;
    });

    esUsuario = computed(() => this.autenticado() && this.tipoSesion() === 'USUARIO');
    esEmpresa = computed(() => this.autenticado() && this.tipoSesion() === 'EMPRESA');

    tipoUsuario = computed(() => {
        const s = this.session();
        return isAutenticado(s) && s.tipoSesion === 'USUARIO' ? s.tipoUsuario : null;
    });

    esAdmin = computed(() => this.esUsuario() && this.tipoUsuario() === 'ADMIN');

    displayName = computed(() => {
        const s = this.session();
        if (!s || s.autenticado !== true) return null;

        if (s.tipoSesion === 'USUARIO') return s.nickname;
        // EMPRESA: en /auth/me solo viene tipoSesion, así que mostramos genérico
        return 'Empresa';
    });

    badgeText = computed(() => {
        const s = this.session();
        if (!s || s.autenticado !== true) return null;

        if (s.tipoSesion === 'USUARIO') return s.tipoUsuario; // COMUN / ADMIN
        return 'EMPRESA';
    });
}
