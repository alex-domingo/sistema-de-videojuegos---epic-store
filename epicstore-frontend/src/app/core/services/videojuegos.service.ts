import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { ApiResponse } from '../models/api.model';
import { VideojuegoListadoItem } from '../models/videojuego.model';
import { VideojuegoDetalle } from '../models/videojuego.model';

@Injectable({ providedIn: 'root' })
export class VideojuegosService {
  constructor(private api: ApiService) { }

  listar() {
    return this.api.get<ApiResponse<VideojuegoListadoItem[]>>('/videojuegos');
  }

  detallePorId(id: number) {
    return this.api.get<ApiResponse<VideojuegoDetalle>>('/videojuegos', { id });
  }
}
