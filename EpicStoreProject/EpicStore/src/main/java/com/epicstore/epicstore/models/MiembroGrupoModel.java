package com.epicstore.epicstore.models;

import com.epicstore.epicstore.util.DBConnection;
import com.epicstore.epicstore.dtos.MiembroGrupoDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MiembroGrupoModel {

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

    public boolean usuarioExiste(Connection conn, int idUsuario) throws Exception {
        String sql = "SELECT 1 FROM USUARIO WHERE id_usuario = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
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

    public boolean yaEsMiembro(Connection conn, int idGrupo, int idUsuario) throws Exception {
        String sql = "SELECT 1 FROM MIEMBRO_GRUPO WHERE id_grupo = ? AND id_usuario = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int contarMiembros(Connection conn, int idGrupo) throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM MIEMBRO_GRUPO WHERE id_grupo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
                return 0;
            }
        }
    }

    // 1) INSERT MIEMBRO_GRUPO (nuevo miembro en grupo)
    public int insertarMiembro(Connection conn, int idGrupo, int idUsuarioNuevo) throws Exception {
        String sql = "INSERT INTO MIEMBRO_GRUPO (id_grupo, id_usuario, rol) VALUES (?, ?, 'MIEMBRO')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGrupo);
            ps.setInt(2, idUsuarioNuevo);
            return ps.executeUpdate();
        }
    }

    public ArrayList<MiembroGrupoDTO> listarMiembros(int idGrupo) {

        String sql = "SELECT mg.id_usuario, u.nickname, mg.rol "
                + "FROM MIEMBRO_GRUPO mg "
                + "JOIN USUARIO u ON mg.id_usuario = u.id_usuario "
                + "WHERE mg.id_grupo = ? "
                + "ORDER BY (mg.rol = 'DUENIO') DESC, u.nickname ASC";

        ArrayList<MiembroGrupoDTO> lista = new ArrayList<>();

        try (Connection conn = abrirConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idGrupo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MiembroGrupoDTO dto = new MiembroGrupoDTO();
                    dto.setIdUsuario(rs.getInt("id_usuario"));
                    dto.setNickname(rs.getString("nickname"));
                    dto.setRol(rs.getString("rol"));
                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al listar miembros: " + e.getMessage());
        }

        return lista;
    }
}
