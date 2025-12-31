export interface ApiResponse<T> {
    total: number;
    datos: T;
    exito: boolean;
    mensaje: string;
}
