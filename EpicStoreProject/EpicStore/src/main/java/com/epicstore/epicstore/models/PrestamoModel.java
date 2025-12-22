package com.epicstore.epicstore.models;

import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrestamoModel {

    public Connection abrirConexion() throws Exception {
        DBConnection db = new DBConnection();
        return db.getConnection();
    }

    public boolean grupoExiste(Connection conn, int idGrupo) throws Exception {
        String sql = "SELECT 1 FROM GRUPO_FAMILIAR WHERE id_grupo = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean esDueno(Connection conn, int idGrupo, int idDueno) throws Exception {
        String sql = "SELECT 1 FROM MIEMBRO_GRUPO WHERE id_grupo = ? AND id_usuario = ? AND rol = 'DUENIO' LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idDueno);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean esMiembroDelGrupo(Connection conn, int idGrupo, int idUsuario) throws Exception {
        String sql = "SELECT 1 FROM MIEMBRO_GRUPO WHERE id_grupo = ? AND id_usuario = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean usuarioExiste(Connection conn, int idUsuario) throws Exception {
        String sql = "SELECT 1 FROM USUARIO WHERE id_usuario = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean videojuegoExiste(Connection conn, int idVideojuego) throws Exception {
        String sql = "SELECT 1 FROM VIDEOJUEGO WHERE id_videojuego = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean duenoTieneJuegoPropio(Connection conn, int idDueno, int idVideojuego) throws Exception {
        String sql = "SELECT 1 FROM BIBLIOTECA_JUEGO "
                + "WHERE id_usuario = ? AND id_videojuego = ? AND tipo_propiedad = 'PROPIO' "
                + "LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDueno);
            ps.setInt(2, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean receptorYaTieneJuego(Connection conn, int idReceptor, int idVideojuego) throws Exception {
        String sql = "SELECT 1 FROM BIBLIOTECA_JUEGO WHERE id_usuario = ? AND id_videojuego = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReceptor);
            ps.setInt(2, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // 1) INSERT BIBLIOTECA_JUEGO (en biblioteca propia de algún miembro)
    public int insertarPrestamo(Connection conn, int idReceptor, int idVideojuego, int idGrupo) throws Exception {
        String sql = "INSERT INTO BIBLIOTECA_JUEGO "
                + "(id_usuario, id_videojuego, id_grupo_origen, tipo_propiedad, estado_instalacion, fecha_ultimo_cambio_estado) "
                + "VALUES (?, ?, ?, 'PRESTADO', 'NO_INSTALADO', NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReceptor);
            ps.setInt(2, idVideojuego);
            ps.setInt(3, idGrupo);
            return ps.executeUpdate();
        }
    }
}
