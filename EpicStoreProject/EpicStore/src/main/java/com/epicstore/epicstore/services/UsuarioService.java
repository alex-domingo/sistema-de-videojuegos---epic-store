package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.RegistroUsuarioDTO;
import com.epicstore.epicstore.dtos.UsuarioBusquedaDTO;
import com.epicstore.epicstore.models.UsuarioModel;

import java.sql.Date;
import java.util.ArrayList;

public class UsuarioService {

    private final UsuarioModel model = new UsuarioModel();

    public static final int OK = 1;
    public static final int ERROR = -1;
    public static final int CORREO_YA_EXISTE = -2;
    public static final int NICKNAME_YA_EXISTE = -3;

    public static class ResultadoRegistro {

        public int codigo;
        public String mensaje;
        public Integer idUsuario;
    }

    public ResultadoRegistro registrar(RegistroUsuarioDTO dto) {
        ResultadoRegistro r = new ResultadoRegistro();

        if (dto == null) {
            r.codigo = ERROR;
            r.mensaje = "JSON inválido";
            return r;
        }

        if (dto.getNickname() == null || dto.getNickname().trim().isEmpty()
                || dto.getPassword() == null || dto.getPassword().trim().isEmpty()
                || dto.getCorreo() == null || dto.getCorreo().trim().isEmpty()
                || dto.getTelefono() == null || dto.getTelefono().trim().isEmpty()
                || dto.getPais() == null || dto.getPais().trim().isEmpty()
                || dto.getFechaNacimiento() == null || dto.getFechaNacimiento().trim().isEmpty()) {
            r.codigo = ERROR;
            r.mensaje = "Faltan campos obligatorios";
            return r;
        }

        boolean bibliotecaPublica = dto.getBibliotecaPublica() == null ? true : dto.getBibliotecaPublica();

        try {
            Date fn = Date.valueOf(dto.getFechaNacimiento()); // yyyy-MM-dd
            int id = model.registrarUsuario(
                    dto.getNickname().trim(),
                    dto.getPassword(),
                    fn,
                    dto.getCorreo().trim(),
                    dto.getTelefono().trim(),
                    dto.getPais().trim(),
                    dto.getAvatar(),
                    bibliotecaPublica
            );

            if (id == CORREO_YA_EXISTE) {
                r.codigo = CORREO_YA_EXISTE;
                r.mensaje = "El correo ya está registrado";
                return r;
            }
            if (id == NICKNAME_YA_EXISTE) {
                r.codigo = NICKNAME_YA_EXISTE;
                r.mensaje = "El nickname ya está en uso";
                return r;
            }

            if (id > 0) {
                r.codigo = OK;
                r.mensaje = "Usuario registrado correctamente";
                r.idUsuario = id;
                return r;
            }

            r.codigo = ERROR;
            r.mensaje = "No se pudo registrar el usuario";
            return r;

        } catch (IllegalArgumentException e) {
            r.codigo = ERROR;
            r.mensaje = "Formato de fechaNacimiento inválido (usa yyyy-MM-dd)";
            return r;
        } catch (Exception e) {
            r.codigo = ERROR;
            r.mensaje = "Error: " + e.getMessage();
            return r;
        }
    }

    public ArrayList<UsuarioBusquedaDTO> buscar(String nickname) {
        try {
            return model.buscarPorNickname(nickname);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
