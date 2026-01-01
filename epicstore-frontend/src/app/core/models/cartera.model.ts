export type TipoMovimientoCartera = 'RECARGA' | 'COMPRA' | 'REEMBOLSO' | 'OTRO';

export interface MovimientoCartera {
    idMovimiento: number;
    tipo: string; // lo dejamos string por si hay más tipos además de RECARGA
    fecha: string; // "YYYY-MM-DD HH:mm:ss.S"
    monto: number;
    saldoResultante: number;
    descripcion: string;
}

export interface CarteraMovimientosResponse {
    exito: boolean;
    mensaje: string;
    saldoActual?: number;
    totalMovimientos?: number;
    movimientos?: MovimientoCartera[];
}

export interface RecargaRequest {
    idUsuario: number;
    monto: number;
    descripcion: string;
}

export interface RecargaResponse {
    exito: boolean;
    mensaje: string;
    saldoAnterior?: number;
    saldoNuevo?: number;
}
