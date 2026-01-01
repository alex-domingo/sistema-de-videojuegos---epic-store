export type SN = 'S' | 'N';

export interface EmpresaVideojuegoItem {
    idVideojuego: number;
    idEmpresa: number;
    idClasificacion: number;
    titulo: string;
    descripcion: string;
    precio: number;
    requisitosMinimos: string;
    fechaLanzamiento: string; // YYYY-MM-DD
    imagenPortada: string;

    ventaActiva: SN;
    comentariosVisibles: SN;

    codigoClasificacion: string; // "T", "M", etc.
    edadMinima: number;

    calificacionPromedio?: number; // a veces viene
    totalVentas: number;
}

export interface EmpresaCatalogoResponse {
    datos: EmpresaVideojuegoItem[];
    exito: boolean;
    mensaje: string;
}

export interface ApiMsg {
    exito: boolean;
    mensaje: string;
}
