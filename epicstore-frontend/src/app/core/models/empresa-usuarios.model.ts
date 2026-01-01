export interface UsuarioEmpresaItem {
    idUsuarioEmpresa: number;
    nombre: string;
    correo: string;
    fechaNacimiento: string; // yyyy-mm-dd
}

export interface CrearUsuarioEmpresaRequest {
    nombre: string;
    correo: string;
    fechaNacimiento: string; // yyyy-mm-dd
    password: string;
}

export interface ApiMsg {
    exito: boolean;
    mensaje: string;
    datos?: any;
}

export interface ListadoUsuariosEmpresaResponse {
    exito: boolean;
    mensaje: string;
    datos: UsuarioEmpresaItem[];
}
