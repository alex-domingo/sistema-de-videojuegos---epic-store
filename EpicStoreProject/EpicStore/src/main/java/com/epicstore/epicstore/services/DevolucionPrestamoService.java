package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.DevolverPrestamoDTO;
import com.epicstore.epicstore.models.DevolucionPrestamoModel;

import java.sql.Connection;

public class DevolucionPrestamoService {

    private final DevolucionPrestamoModel model = new DevolucionPrestamoModel();

    public static final int OK = 1;
    public static final int ERROR = -1;
    public static final int GRUPO_NO_EXISTE = -2;
    public static final int DUENO_NO_VALIDO = -3;
    public static final int PRESTAMO_NO_EXISTE = -4;

    public static class Resultado {

        public int codigo;
        public String mensaje;
    }

    public Resultado devolver(DevolverPrestamoDTO dto) {

        Resultado r = new Resultado();

        if (dto == null || dto.getIdGrupo() <= 0 || dto.getIdDueno() <= 0
                || dto.getIdReceptor() <= 0 || dto.getIdVideojuego() <= 0) {
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
                r.codigo = DUENO_NO_VALIDO;
                r.mensaje = "Solo el dueño del grupo puede devolver préstamos";
                return r;
            }

            var prestamo = model.obtenerPrestamo(conn, dto.getIdGrupo(), dto.getIdReceptor(), dto.getIdVideojuego());
            if (prestamo == null) {
                conn.rollback();
                r.codigo = PRESTAMO_NO_EXISTE;
                r.mensaje = "No existe un préstamo activo para esos datos";
                return r;
            }

            // Si estaba instalado, lo ponemos NO_INSTALADO antes de borrar
            if ("INSTALADO".equalsIgnoreCase(prestamo.estadoInstalacion)) {
                model.marcarNoInstalado(conn, prestamo.idBibliotecaJuego);
            }

            int filas = model.eliminarPrestamo(conn, prestamo.idBibliotecaJuego);
            if (filas == 0) {
                conn.rollback();
                r.codigo = ERROR;
                r.mensaje = "No se pudo devolver el préstamo";
                return r;
            }

            conn.commit();
            r.codigo = OK;
            r.mensaje = "Préstamo devuelto correctamente";
            return r;

        } catch (Exception e) {
            r.codigo = ERROR;
            r.mensaje = "Error: " + e.getMessage();
            return r;
        }
    }
}
