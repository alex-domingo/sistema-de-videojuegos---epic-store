export interface VideojuegoListadoItem {
    idVideojuego: number;
    titulo: string;
    precio: number;
    imagenPortada: string;
    empresa: string;
    clasificacion: string;
    edadMinima: number;
    calificacionPromedio: number;
}

export interface VideojuegoDetalle {
    idVideojuego: number;
    titulo: string;
    descripcion: string;
    precio: number;
    requisitosMinimos: string;
    fechaLanzamiento: string; // viene como "YYYY-MM-DD"
    imagenPortada: string;
    empresa: string;
    clasificacion: string;
    edadMinima: number;
    categorias: string[];
    calificacionPromedio: number;
}

