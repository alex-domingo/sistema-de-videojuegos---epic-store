import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import type { ComentariosResponse } from '../models/comentarios.model';

@Injectable({ providedIn: 'root' })
export class ComentariosService {
    constructor(private api: ApiService) { }

    listar(idVideojuego: number) {
        return this.api.get<ComentariosResponse>('/comentarios', { idVideojuego });
    }

    crear(body: {
        idUsuario: number;
        idVideojuego: number;
        calificacion: number;
        texto: string;
        idComentarioPadre: number | null;
    }) {
        return this.api.post<any>('/comentarios', body);
    }
}
