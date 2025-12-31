export type TipoSesion = 'USUARIO' | 'EMPRESA';
export type TipoUsuario = 'COMUN' | 'ADMIN';

export interface LoginUsuarioRequest {
    login: string;
    password: string;
}

export interface LoginUsuarioResponse {
    exito: boolean;
    idUsuario: number;
    nickname: string;
    tipoUsuario: TipoUsuario;
    mensaje: string;
    tipoSesion: 'USUARIO';
}

export interface LoginEmpresaRequest {
    correo: string;
    password: string;
}

export interface LoginEmpresaResponse {
    exito: boolean;
    idUsuarioEmpresa: number;
    correo: string;
    idEmpresa: number;
    nombre: string;
    mensaje: string;
    tipoSesion: 'EMPRESA';
}

export interface AuthMeDatosAutenticadoUsuario {
    autenticado: true;
    tipoSesion: 'USUARIO';
    idUsuario: number;
    nickname: string;
    tipoUsuario: TipoUsuario;
}

export interface AuthMeDatosAutenticadoEmpresa {
    autenticado: true;
    tipoSesion: 'EMPRESA';
    idUsuarioEmpresa?: number;
    idEmpresa?: number;
    correo?: string;
    nombre?: string;
}

export interface AuthMeDatosNoAutenticado {
    autenticado: false;
}

export interface AuthMeResponse {
    exito: boolean;
    datos: AuthMeDatosAutenticadoUsuario | AuthMeDatosAutenticadoEmpresa | AuthMeDatosNoAutenticado;
}

export interface LogoutResponse {
    exito: boolean;
    mensaje: string;
}
