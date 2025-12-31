import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { ApiResponse } from '../models/api.model';
import { BibliotecaItem } from '../models/biblioteca.model';
import { ActualizarEstadoRequest, ActualizarEstadoResponse } from '../models/biblioteca-estado.model';

@Injectable({ providedIn: 'root' })
export class BibliotecaService {
    constructor(private api: ApiService) { }

    listarPorUsuario(idUsuario: number) {
        return this.api.get<ApiResponse<BibliotecaItem[]>>('/biblioteca', { idUsuario });
    }

    actualizarEstado(body: ActualizarEstadoRequest) {
        return this.api.put<ActualizarEstadoResponse>('/biblioteca/estado', body);
    }
}
