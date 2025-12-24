package com.epicstore.epicstore.models;

import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GrupoAdminModel {

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

    public boolean esMiembro(Connection conn, int idGrupo, int idUsuario) throws Exception {
        String sql = "SELECT 1 FROM MIEMBRO_GRUPO WHERE id_grupo = ? AND id_usuario = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Si el miembro tiene prestados instalados, primero los pasamos a NO_INSTALADO
    public int marcarPrestadosNoInstalado(Connection conn, int idGrupo, int idUsuario) throws Exception {
        String sql = "UPDATE BIBLIOTECA_JUEGO "
                + "SET estado_instalacion = 'NO_INSTALADO', fecha_ultimo_cambio_estado = NOW() "
                + "WHERE id_usuario = ? AND tipo_propiedad = 'PRESTADO' AND id_grupo_origen = ? AND estado_instalacion = 'INSTALADO'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idGrupo);
            return ps.executeUpdate();
        }
    }

    // Devolvemos todos los préstamos del grupo para ese usuario (borra filas PRESTADO)
    public int eliminarPrestamosDeUsuario(Connection conn, int idGrupo, int idUsuario) throws Exception {
        String sql = "DELETE FROM BIBLIOTECA_JUEGO "
                + "WHERE id_usuario = ? AND tipo_propiedad = 'PRESTADO' AND id_grupo_origen = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idGrupo);
            return ps.executeUpdate();
        }
    }

    public int eliminarMiembro(Connection conn, int idGrupo, int idUsuario) throws Exception {
        String sql = "DELETE FROM MIEMBRO_GRUPO WHERE id_grupo = ? AND id_usuario = ? AND rol <> 'DUENIO'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate();
        }
    }

    // Para eliminar grupo hacemos:
    public int marcarPrestadosGrupoNoInstalado(Connection conn, int idGrupo) throws Exception {
        String sql = "UPDATE BIBLIOTECA_JUEGO "
                + "SET estado_instalacion = 'NO_INSTALADO', fecha_ultimo_cambio_estado = NOW() "
                + "WHERE tipo_propiedad = 'PRESTADO' AND id_grupo_origen = ? AND estado_instalacion = 'INSTALADO'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            return ps.executeUpdate();
        }
    }

    public int eliminarPrestamosGrupo(Connection conn, int idGrupo) throws Exception {
        String sql = "DELETE FROM BIBLIOTECA_JUEGO WHERE tipo_propiedad = 'PRESTADO' AND id_grupo_origen = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            return ps.executeUpdate();
        }
    }

    public int eliminarMiembrosGrupo(Connection conn, int idGrupo) throws Exception {
        String sql = "DELETE FROM MIEMBRO_GRUPO WHERE id_grupo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            return ps.executeUpdate();
        }
    }

    public int eliminarGrupo(Connection conn, int idGrupo) throws Exception {
        String sql = "DELETE FROM GRUPO_FAMILIAR WHERE id_grupo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            return ps.executeUpdate();
        }
    }
}
