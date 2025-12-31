import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE } from '../config/api.config';
import {
    LoginUsuarioRequest, LoginUsuarioResponse,
    LoginEmpresaRequest, LoginEmpresaResponse,
    AuthMeResponse, LogoutResponse
} from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
    constructor(private http: HttpClient) { }

    loginUsuario(body: LoginUsuarioRequest) {
        return this.http.post<LoginUsuarioResponse>(
            `${API_BASE}/auth/login`,
            body,
            { withCredentials: true }
        );
    }

    loginEmpresa(body: LoginEmpresaRequest) {
        return this.http.post<LoginEmpresaResponse>(
            `${API_BASE}/auth/login-empresa`,
            body,
            { withCredentials: true }
        );
    }

    me() {
        return this.http.get<AuthMeResponse>(
            `${API_BASE}/auth/me`,
            { withCredentials: true }
        );
    }

    logout() {
        return this.http.post<LogoutResponse>(
            `${API_BASE}/auth/logout`,
            {},
            { withCredentials: true }
        );
    }
}
