package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.AgregarMiembroGrupoDTO;
import com.epicstore.epicstore.dtos.MiembroGrupoDTO;
import com.epicstore.epicstore.models.MiembroGrupoModel;

import java.sql.Connection;
import java.util.ArrayList;

public class MiembroGrupoService {

    private final MiembroGrupoModel model = new MiembroGrupoModel();

    public static final int OK = 1;
    public static final int ERROR = -1;
    public static final int GRUPO_NO_EXISTE = -2;
    public static final int DUENO_NO_VALIDO = -3;
    public static final int USUARIO_NO_EXISTE = -4;
    public static final int YA_ES_MIEMBRO = -5;
    public static final int LIMITE_MIEMBROS = -6;

    public static class Resultado {

        public int codigo;
        public String mensaje;
    }

    public Resultado agregarMiembro(AgregarMiembroGrupoDTO dto) {

        Resultado r = new Resultado();

        if (dto == null || dto.getIdGrupo() <= 0 || dto.getIdDueno() <= 0 || dto.getIdUsuarioNuevo() <= 0) {
            r.codigo = ERROR;
            r.mensaje = "Datos inválidos";
            return r;
        }

        // No permitimos que el dueño "se agregue" a sí mismo (ya debe estar como DUENIO)
        if (dto.getIdDueno() == dto.getIdUsuarioNuevo()) {
            r.codigo = YA_ES_MIEMBRO;
            r.mensaje = "El dueño ya pertenece al grupo";
            return r;
        }

        try (Connection conn = model.abrirConexion()) {

            if (!model.grupoExiste(conn, dto.getIdGrupo())) {
                r.codigo = GRUPO_NO_EXISTE;
                r.mensaje = "El grupo no existe";
                return r;
            }

            if (!model.esDueno(conn, dto.getIdGrupo(), dto.getIdDueno())) {
                r.codigo = DUENO_NO_VALIDO;
                r.mensaje = "Solo el dueño del grupo puede agregar miembros";
                return r;
            }

            if (!model.usuarioExiste(conn, dto.getIdUsuarioNuevo())) {
                r.codigo = USUARIO_NO_EXISTE;
                r.mensaje = "El usuario a agregar no existe";
                return r;
            }

            if (model.yaEsMiembro(conn, dto.getIdGrupo(), dto.getIdUsuarioNuevo())) {
                r.codigo = YA_ES_MIEMBRO;
                r.mensaje = "El usuario ya es miembro del grupo";
                return r;
            }

            int total = model.contarMiembros(conn, dto.getIdGrupo());
            if (total >= 6) {
                r.codigo = LIMITE_MIEMBROS;
                r.mensaje = "El grupo ya alcanzó el límite máximo de 6 miembros";
                return r;
            }

            int filas = model.insertarMiembro(conn, dto.getIdGrupo(), dto.getIdUsuarioNuevo());
            if (filas > 0) {
                r.codigo = OK;
                r.mensaje = "Miembro agregado correctamente";
            } else {
                r.codigo = ERROR;
                r.mensaje = "No se pudo agregar el miembro";
            }

            return r;

        } catch (Exception e) {
            r.codigo = ERROR;
            r.mensaje = "Error: " + e.getMessage();
            return r;
        }
    }

    public ArrayList<MiembroGrupoDTO> listarMiembros(int idGrupo) {
        return model.listarMiembros(idGrupo);
    }
}
