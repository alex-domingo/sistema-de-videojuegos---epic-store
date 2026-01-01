import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { CrearPrestamoRequest, PrestamoResponse } from '../models/prestamos.model';

@Injectable({ providedIn: 'root' })
export class PrestamosService {
    constructor(private api: ApiService) { }

    prestar(body: CrearPrestamoRequest) {
        return this.api.post<PrestamoResponse>('/prestamos', body);
    }

    devolver(body: CrearPrestamoRequest) {
        return this.api.post<PrestamoResponse>('/prestamos/devolver', body);
    }
}
