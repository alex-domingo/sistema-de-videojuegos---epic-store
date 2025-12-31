import type { EstadoInstalacion } from './biblioteca.model';

export interface ActualizarEstadoRequest {
    idUsuario: number;
    idVideojuego: number;
    nuevoEstado: EstadoInstalacion;
}

export interface ActualizarEstadoResponse {
    exito: boolean;
    mensaje: string;
}
