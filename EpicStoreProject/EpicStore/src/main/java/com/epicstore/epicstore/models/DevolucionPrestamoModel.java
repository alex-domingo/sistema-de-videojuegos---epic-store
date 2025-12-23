package com.epicstore.epicstore.models;

import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DevolucionPrestamoModel {

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

    public PrestamoInfo obtenerPrestamo(Connection conn, int idGrupo, int idReceptor, int idVideojuego) throws Exception {
        String sql = "SELECT id_biblioteca_juego, estado_instalacion "
                + "FROM BIBLIOTECA_JUEGO "
                + "WHERE id_usuario = ? AND id_videojuego = ? "
                + "  AND tipo_propiedad = 'PRESTADO' "
                + "  AND id_grupo_origen = ? "
                + "LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReceptor);
            ps.setInt(2, idVideojuego);
            ps.setInt(3, idGrupo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PrestamoInfo p = new PrestamoInfo();
                    p.idBibliotecaJuego = rs.getInt("id_biblioteca_juego");
                    p.estadoInstalacion = rs.getString("estado_instalacion");
                    return p;
                }
            }
        }

        return null;
    }

    // 1) UPDATE BIBLIOTECA_JUEGO (para cambiar estado del videojuego)
    public int marcarNoInstalado(Connection conn, int idBibliotecaJuego) throws Exception {
        String sql = "UPDATE BIBLIOTECA_JUEGO "
                + "SET estado_instalacion = 'NO_INSTALADO', fecha_ultimo_cambio_estado = NOW() "
                + "WHERE id_biblioteca_juego = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBibliotecaJuego);
            return ps.executeUpdate();
        }
    }

    // 2) DELETE BIBLIOTECA_JUEGO (eliminamos de la biblioteca personal del usuario al que hemos prestado)
    public int eliminarPrestamo(Connection conn, int idBibliotecaJuego) throws Exception {
        String sql = "DELETE FROM BIBLIOTECA_JUEGO WHERE id_biblioteca_juego = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBibliotecaJuego);
            return ps.executeUpdate();
        }
    }

    public static class PrestamoInfo {

        public int idBibliotecaJuego;
        public String estadoInstalacion;
    }
}
