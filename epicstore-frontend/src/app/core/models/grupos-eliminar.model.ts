export interface EliminarMiembroRequest {
    idGrupo: number;
    idDueno: number;
    idUsuarioEliminar: number;
}

export interface EliminarGrupoRequest {
    idGrupo: number;
    idDueno: number;
}

export interface RespuestaSimple {
    exito: boolean;
    mensaje: string;
}
