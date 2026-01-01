export interface CrearCompraRequest {
    idUsuario: number;
    idVideojuego: number;
    fechaCompra: string; // "YYYY-MM-DD"
}

export interface CompraResultado {
    idCompra: number;
    saldoAnterior: number;
    saldoNuevo: number;
    precio: number;
    porcentajeComision: number;
    montoComision: number;
    montoEmpresa: number;
}

export interface CrearCompraResponse {
    exito: boolean;
    mensaje: string;
    datos?: CompraResultado;
}
