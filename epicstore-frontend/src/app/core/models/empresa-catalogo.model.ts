export interface EmpresaCatalogoItem {
    idVideojuego: number;
    titulo: string;
    precio: number;
    ventaActiva: 'S' | 'N';
    calificacionPromedio?: number; // a veces viene, a veces no
}

export interface EmpresaPerfilResponse {
    exito: boolean;
    empresa: import('./empresa.model').EmpresaPublica;
    catalogo: EmpresaCatalogoItem[];
}
