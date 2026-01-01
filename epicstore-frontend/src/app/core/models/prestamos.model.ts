export interface CrearPrestamoRequest {
    idGrupo: number;
    idDueno: number;
    idReceptor: number;
    idVideojuego: number;
}

export interface PrestamoResponse {
    exito: boolean;
    mensaje: string;
}
