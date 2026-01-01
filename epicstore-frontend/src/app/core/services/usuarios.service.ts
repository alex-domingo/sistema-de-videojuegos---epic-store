import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { RegistroUsuarioRequest, RegistroUsuarioResponse } from '../models/registro.model';

@Injectable({ providedIn: 'root' })
export class UsuariosService {
    constructor(private api: ApiService) { }

    registrar(body: RegistroUsuarioRequest) {
        return this.api.post<RegistroUsuarioResponse>('/usuarios/registro', body);
    }
}
