import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import type {
    CrearUsuarioEmpresaRequest,
    ListadoUsuariosEmpresaResponse,
    ApiMsg
} from '../models/empresa-usuarios.model';

@Injectable({ providedIn: 'root' })
export class EmpresaUsuariosService {
    constructor(private api: ApiService) { }

    listar() {
        return this.api.get<ListadoUsuariosEmpresaResponse>('/empresa/usuarios');
    }

    crear(body: CrearUsuarioEmpresaRequest) {
        return this.api.post<ApiMsg>('/empresa/usuarios', body);
    }

    eliminar(idUsuarioEmpresa: number) {
        return this.api.delete<ApiMsg>('/empresa/usuarios', { id: idUsuarioEmpresa });
    }
}
