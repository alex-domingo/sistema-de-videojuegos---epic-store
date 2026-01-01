import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import type { EmpresaCatalogoResponse, ApiMsg, SN } from '../models/empresa-catalogo.model';

@Injectable({ providedIn: 'root' })
export class EmpresaCatalogoService {
    constructor(private api: ApiService) { }

    listar() {
        return this.api.get<EmpresaCatalogoResponse>('/empresa/videojuegos');
    }

    // toggle venta
    setVenta(idVideojuego: number, ventaActiva: SN) {
        return this.api.put<ApiMsg>('/empresa/videojuegos/venta', { idVideojuego, ventaActiva });
    }

    setComentariosVisibles(idVideojuego: number, comentariosVisibles: SN) {
        return this.api.put<ApiMsg>('/empresa/videojuegos/comentarios', { idVideojuego, comentariosVisibles });
    }
}
