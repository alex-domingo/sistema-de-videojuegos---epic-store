package com.epicstore.epicstore.models;

import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BibliotecaEstadoModel {

    public boolean existeRegistroBiblioteca(Connection conn, int idUsuario, int idVideojuego) throws Exception {
        String sql = "SELECT 1 FROM BIBLIOTECA_JUEGO WHERE id_usuario = ? AND id_videojuego = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public String obtenerTipoPropiedad(Connection conn, int idUsuario, int idVideojuego) throws Exception {
        String sql = "SELECT tipo_propiedad FROM BIBLIOTECA_JUEGO WHERE id_usuario = ? AND id_videojuego = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tipo_propiedad");
                }
                return null;
            }
        }
    }

    public int contarPrestadosInstalados(Connection conn, int idUsuario) throws Exception {
        String sql = "SELECT COUNT(*) AS total "
                + "FROM BIBLIOTECA_JUEGO "
                + "WHERE id_usuario = ? AND tipo_propiedad = 'PRESTADO' AND estado_instalacion = 'INSTALADO'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return 0;
            }
        }
    }

    // 1) Update BIBLIOTECA_JUEGO estado_instalacion
    public int actualizarEstado(Connection conn, int idUsuario, int idVideojuego, String nuevoEstado) throws Exception {
        String sql = "UPDATE BIBLIOTECA_JUEGO "
                + "SET estado_instalacion = ?, fecha_ultimo_cambio_estado = NOW() "
                + "WHERE id_usuario = ? AND id_videojuego = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idVideojuego);
            return ps.executeUpdate();
        }
    }

    public Connection abrirConexion() throws Exception {
        DBConnection db = new DBConnection();
        return db.getConnection();
    }
}
