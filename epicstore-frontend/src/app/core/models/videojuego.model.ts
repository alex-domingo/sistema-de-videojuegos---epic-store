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
    idEmpresa: number; // ✅ nuevo
    titulo: string;
    descripcion: string;
    precio: number;
    requisitosMinimos: string;
    fechaLanzamiento: string;
    imagenPortada: string;
    empresa: string;
    clasificacion: string;
    edadMinima: number;
    categorias: string[];
    calificacionPromedio: number;
}
