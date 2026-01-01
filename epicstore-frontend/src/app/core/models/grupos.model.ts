export type RolGrupo = 'DUENIO' | 'MIEMBRO';

export interface GrupoItem {
    idGrupo: number;
    nombreGrupo: string;
    rol: RolGrupo;
    totalMiembros: number;
}

export interface ApiListaGrupos {
    total: number;
    datos: GrupoItem[];
    exito: boolean;
    mensaje: string;
}

export interface CrearGrupoRequest {
    idDueno: number;
    nombreGrupo: string;
}

export interface CrearGrupoResponse {
    exito: boolean;
    mensaje: string;
    idGrupo?: number;
}

export interface AgregarMiembroRequest {
    idGrupo: number;
    idDueno: number;
    idUsuarioNuevo: number;
}

export interface RespuestaSimple {
    exito: boolean;
    mensaje: string;
}

export interface MiembroGrupo {
    idUsuario: number;
    nickname: string;
    rol: RolGrupo;
}

export interface ListaMiembrosResponse {
    total: number;
    exito: boolean;
    mensaje: string;
    miembros: MiembroGrupo[];
}
