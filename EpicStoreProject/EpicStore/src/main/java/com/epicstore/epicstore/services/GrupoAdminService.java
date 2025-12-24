package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.EliminarMiembroDTO;
import com.epicstore.epicstore.dtos.EliminarGrupoDTO;
import com.epicstore.epicstore.models.GrupoAdminModel;

import java.sql.Connection;

public class GrupoAdminService {

    private final GrupoAdminModel model = new GrupoAdminModel();

    public static final int OK = 1;
    public static final int ERROR = -1;
    public static final int GRUPO_NO_EXISTE = -2;
    public static final int NO_ES_DUENO = -3;
    public static final int MIEMBRO_NO_EXISTE = -4;
    public static final int NO_SE_PUEDE_ELIMINAR_DUENO = -5;

    public static class Resultado {

        public int codigo;
        public String mensaje;
    }

    public Resultado eliminarMiembro(EliminarMiembroDTO dto) {
        Resultado r = new Resultado();

        if (dto == null || dto.getIdGrupo() <= 0 || dto.getIdDueno() <= 0 || dto.getIdUsuarioEliminar() <= 0) {
            r.codigo = ERROR;
            r.mensaje = "Datos inválidos";
            return r;
        }

        if (dto.getIdDueno() == dto.getIdUsuarioEliminar()) {
            r.codigo = NO_SE_PUEDE_ELIMINAR_DUENO;
            r.mensaje = "No se puede eliminar al dueño del grupo";
            return r;
        }

        try (Connection conn = model.abrirConexion()) {
            conn.setAutoCommit(false);

            if (!model.grupoExiste(conn, dto.getIdGrupo())) {
                conn.rollback();
                r.codigo = GRUPO_NO_EXISTE;
                r.mensaje = "El grupo no existe";
                return r;
            }

            if (!model.esDueno(conn, dto.getIdGrupo(), dto.getIdDueno())) {
                conn.rollback();
                r.codigo = NO_ES_DUENO;
                r.mensaje = "Solo el dueño del grupo puede eliminar miembros";
                return r;
            }

            if (!model.esMiembro(conn, dto.getIdGrupo(), dto.getIdUsuarioEliminar())) {
                conn.rollback();
                r.codigo = MIEMBRO_NO_EXISTE;
                r.mensaje = "El usuario no pertenece al grupo";
                return r;
            }

            // 1) Devolvemos préstamos del miembro (si estaban instalados, bajarlos)
            model.marcarPrestadosNoInstalado(conn, dto.getIdGrupo(), dto.getIdUsuarioEliminar());
            model.eliminarPrestamosDeUsuario(conn, dto.getIdGrupo(), dto.getIdUsuarioEliminar());

            // 2) Eliminamos miembro
            int filas = model.eliminarMiembro(conn, dto.getIdGrupo(), dto.getIdUsuarioEliminar());
            if (filas == 0) {
                conn.rollback();
                r.codigo = ERROR;
                r.mensaje = "No se pudo eliminar el miembro (verifica que no sea dueño)";
                return r;
            }

            conn.commit();
            r.codigo = OK;
            r.mensaje = "Miembro eliminado correctamente";
            return r;

        } catch (Exception e) {
            r.codigo = ERROR;
            r.mensaje = "Error: " + e.getMessage();
            return r;
        }
    }

    public Resultado eliminarGrupo(EliminarGrupoDTO dto) {
        Resultado r = new Resultado();

        if (dto == null || dto.getIdGrupo() <= 0 || dto.getIdDueno() <= 0) {
            r.codigo = ERROR;
            r.mensaje = "Datos inválidos";
            return r;
        }

        try (Connection conn = model.abrirConexion()) {
            conn.setAutoCommit(false);

            if (!model.grupoExiste(conn, dto.getIdGrupo())) {
                conn.rollback();
                r.codigo = GRUPO_NO_EXISTE;
                r.mensaje = "El grupo no existe";
                return r;
            }

            if (!model.esDueno(conn, dto.getIdGrupo(), dto.getIdDueno())) {
                conn.rollback();
                r.codigo = NO_ES_DUENO;
                r.mensaje = "Solo el dueño puede eliminar el grupo";
                return r;
            }

            // 1) Devolvemos TODOS los préstamos del grupo (si están en INSTALADO pasan a NO_INSTALADO)
            model.marcarPrestadosGrupoNoInstalado(conn, dto.getIdGrupo());
            model.eliminarPrestamosGrupo(conn, dto.getIdGrupo());

            // 2) Eliminamos miembros
            model.eliminarMiembrosGrupo(conn, dto.getIdGrupo());

            // 3) Eliminamos grupo
            int filas = model.eliminarGrupo(conn, dto.getIdGrupo());
            if (filas == 0) {
                conn.rollback();
                r.codigo = ERROR;
                r.mensaje = "No se pudo eliminar el grupo";
                return r;
            }

            conn.commit();
            r.codigo = OK;
            r.mensaje = "Grupo eliminado correctamente";
            return r;

        } catch (Exception e) {
            r.codigo = ERROR;
            r.mensaje = "Error: " + e.getMessage();
            return r;
        }
    }
}
