import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import {
    ApiListaGrupos,
    CrearGrupoRequest,
    CrearGrupoResponse,
    AgregarMiembroRequest,
    RespuestaSimple,
    ListaMiembrosResponse,
} from '../models/grupos.model';

import { EliminarMiembroRequest, EliminarGrupoRequest } from '../models/grupos-eliminar.model';

@Injectable({ providedIn: 'root' })
export class GruposService {
    constructor(private api: ApiService) { }

    listarGrupos(idUsuario: number) {
        return this.api.get<ApiListaGrupos>('/grupos', { idUsuario });
    }

    crearGrupo(body: CrearGrupoRequest) {
        return this.api.post<CrearGrupoResponse>('/grupos', body);
    }

    listarMiembros(idGrupo: number) {
        return this.api.get<ListaMiembrosResponse>('/grupos/miembros', { idGrupo });
    }

    agregarMiembro(body: AgregarMiembroRequest) {
        return this.api.post<RespuestaSimple>('/grupos/miembros', body);
    }

    eliminarMiembro(body: EliminarMiembroRequest) {
        return this.api.deleteWithBody<RespuestaSimple>('/grupos/miembros', body);
    }

    eliminarGrupo(body: EliminarGrupoRequest) {
        return this.api.deleteWithBody<RespuestaSimple>('/grupos', body);
    }

}
