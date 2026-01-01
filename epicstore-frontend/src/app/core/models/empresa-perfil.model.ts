export interface EmpresaPerfil {
    idEmpresa: number;
    nombre: string;
    descripcion: string;
    comentariosVisiblesGlobal: 'S' | 'N';
}

export interface EmpresaPerfilJuego {
    idVideojuego: number;
    titulo: string;
    precio: number;
    ventaActiva: 'S' | 'N';
    calificacionPromedio?: number;
}

export interface EmpresaPerfilResponse {
    exito: boolean;
    catalogo: EmpresaPerfilJuego[];
    empresa: EmpresaPerfil;
}
