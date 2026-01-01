export interface ComentarioItemApi {
    idComentario: number;
    idVideojuego: number;
    idUsuario: number;
    idComentarioPadre?: number; // viene solo si es respuesta
    nickname: string;
    fecha: string; // viene con espacios tipo "2025- 12 - 02 09:00:00.0"
    calificacion: number;
    texto: string;
    textoVisible: boolean;
}

export interface ComentariosResponse {
    total: number;
    datos: ComentarioItemApi[];
    exito: boolean;
    mensaje: string;
}

// Frontend-only: para hilo
export interface ComentarioNodo extends ComentarioItemApi {
    respuestas: ComentarioNodo[];
}
