import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { CarteraMovimientosResponse, RecargaRequest, RecargaResponse } from '../models/cartera.model';

@Injectable({ providedIn: 'root' })
export class CarteraService {
    constructor(private api: ApiService) { }

    movimientos(idUsuario: number) {
        return this.api.get<CarteraMovimientosResponse>('/cartera/movimientos', { idUsuario });
    }

    recargar(body: RecargaRequest) {
        return this.api.post<RecargaResponse>('/cartera/recarga', body);
    }
}
