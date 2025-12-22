package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.CrearPrestamoDTO;
import com.epicstore.epicstore.models.PrestamoModel;

import java.sql.Connection;

public class PrestamoService {

    private final PrestamoModel model = new PrestamoModel();

    public static final int OK = 1;
    public static final int ERROR = -1;
    public static final int GRUPO_NO_EXISTE = -2;
    public static final int DUENO_NO_VALIDO = -3;
    public static final int USUARIO_NO_EXISTE = -4;
    public static final int RECEPTOR_NO_ES_MIEMBRO = -5;
    public static final int JUEGO_NO_EXISTE = -6;
    public static final int DUENO_NO_TIENE_JUEGO = -7;
    public static final int RECEPTOR_YA_TIENE_JUEGO = -8;

    public static class Resultado {

        public int codigo;
        public String mensaje;
    }

    public Resultado prestar(CrearPrestamoDTO dto) {

        Resultado r = new Resultado();

        if (dto == null || dto.getIdGrupo() <= 0 || dto.getIdDueno() <= 0
                || dto.getIdReceptor() <= 0 || dto.getIdVideojuego() <= 0) {
            r.codigo = ERROR;
            r.mensaje = "Datos inválidos";
            return r;
        }

        if (dto.getIdDueno() == dto.getIdReceptor()) {
            r.codigo = ERROR;
            r.mensaje = "El dueño no puede prestarse a sí mismo";
            return r;
        }

        try (Connection conn = model.abrirConexion()) {

            // Validaciones
            if (!model.grupoExiste(conn, dto.getIdGrupo())) {
                r.codigo = GRUPO_NO_EXISTE;
                r.mensaje = "El grupo no existe";
                return r;
            }

            if (!model.esDueno(conn, dto.getIdGrupo(), dto.getIdDueno())) {
                r.codigo = DUENO_NO_VALIDO;
                r.mensaje = "Solo el dueño del grupo puede prestar videojuegos";
                return r;
            }

            if (!model.usuarioExiste(conn, dto.getIdReceptor())) {
                r.codigo = USUARIO_NO_EXISTE;
                r.mensaje = "El receptor no existe";
                return r;
            }

            // El receptor debe pertenecer al grupo si o si
            if (!model.esMiembroDelGrupo(conn, dto.getIdGrupo(), dto.getIdReceptor())) {
                r.codigo = RECEPTOR_NO_ES_MIEMBRO;
                r.mensaje = "El receptor no pertenece al grupo";
                return r;
            }

            if (!model.videojuegoExiste(conn, dto.getIdVideojuego())) {
                r.codigo = JUEGO_NO_EXISTE;
                r.mensaje = "El videojuego no existe";
                return r;
            }

            // El dueño debe tenerlo como PROPIO
            if (!model.duenoTieneJuegoPropio(conn, dto.getIdDueno(), dto.getIdVideojuego())) {
                r.codigo = DUENO_NO_TIENE_JUEGO;
                r.mensaje = "El dueño no posee este videojuego (PROPIO) en su biblioteca";
                return r;
            }

            // El receptor no debe tenerlo ya (propio o prestado)
            if (model.receptorYaTieneJuego(conn, dto.getIdReceptor(), dto.getIdVideojuego())) {
                r.codigo = RECEPTOR_YA_TIENE_JUEGO;
                r.mensaje = "El receptor ya tiene este videojuego en su biblioteca";
                return r;
            }

            // Insertamos préstamo
            int filas = model.insertarPrestamo(conn, dto.getIdReceptor(), dto.getIdVideojuego(), dto.getIdGrupo());

            if (filas > 0) {
                r.codigo = OK;
                r.mensaje = "Préstamo realizado correctamente";
            } else {
                r.codigo = ERROR;
                r.mensaje = "No se pudo registrar el préstamo";
            }

            return r;

        } catch (Exception e) {
            r.codigo = ERROR;
            r.mensaje = "Error: " + e.getMessage();
            return r;
        }
    }
}
