import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { CrearCompraRequest, CrearCompraResponse } from '../models/compra.model';

@Injectable({ providedIn: 'root' })
export class ComprasService {
    constructor(private api: ApiService) { }

    comprar(body: CrearCompraRequest) {
        return this.api.post<CrearCompraResponse>('/compras', body);
    }
}
