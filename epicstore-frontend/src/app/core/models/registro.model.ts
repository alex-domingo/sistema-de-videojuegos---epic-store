export interface RegistroUsuarioRequest {
    nickname: string;
    password: string;
    fechaNacimiento: string; // YYYY-MM-DD (lo mandamos limpio)
    correo: string;
    telefono: string;
    pais: string;
    avatar: string;
    bibliotecaPublica: boolean;
}

export interface RegistroUsuarioResponse {
    exito: boolean;
    idUsuario?: number;
    mensaje: string;
}
