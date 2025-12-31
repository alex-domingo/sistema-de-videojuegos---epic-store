export type TipoPropiedad = 'PROPIO' | 'PRESTADO';
export type EstadoInstalacion = 'INSTALADO' | 'NO_INSTALADO';

export interface BibliotecaItem {
    idBibliotecaJuego: number;
    idVideojuego: number;
    titulo: string;
    imagenPortada: string;
    precio: number;
    empresa: string;
    tipoPropiedad: TipoPropiedad;
    estadoInstalacion: EstadoInstalacion;
    fechaUltimoCambioEstado: string; // "YYYY-MM-DD HH:mm:ss.S"
}
