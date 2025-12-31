import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { EmpresaPerfilResponse } from '../models/empresa-catalogo.model';

@Injectable({ providedIn: 'root' })
export class EmpresasService {
    constructor(private api: ApiService) { }

    perfilPublico(idEmpresa: number) {
        return this.api.get<EmpresaPerfilResponse>('/empresas/perfil', { idEmpresa });
    }
}
