package com.epicstore.epicstore.models;

import com.epicstore.epicstore.util.DBConnection;
import com.epicstore.epicstore.dtos.GrupoResumenDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GrupoModel {

    public boolean usuarioExiste(Connection conn, int idUsuario) throws Exception {
        String sql = "SELECT 1 FROM USUARIO WHERE id_usuario = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /*
    Creamos un grupo y registramos al dueño como MIEMBRO_GRUPO con rol DUENIO
    y retornamos id_grupo (>0) si ok, -2 si dueño no existe, -1 si error
     */
    public int crearGrupo(int idDueno, String nombreGrupo) throws Exception {

        DBConnection db = new DBConnection();
        Connection conn = null;

        String sqlGrupo = "INSERT INTO GRUPO_FAMILIAR (nombre_grupo, fecha_creacion) VALUES (?, NOW())";
        String sqlMiembro = "INSERT INTO MIEMBRO_GRUPO (id_grupo, id_usuario, rol) VALUES (?, ?, 'DUENIO')";

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);

            if (!usuarioExiste(conn, idDueno)) {
                conn.rollback();
                return -2;
            }

            int idGrupoGenerado;

            // 1) Insert grupo
            try (PreparedStatement psGrupo = conn.prepareStatement(sqlGrupo, Statement.RETURN_GENERATED_KEYS)) {
                psGrupo.setString(1, nombreGrupo);

                int filas = psGrupo.executeUpdate();
                if (filas == 0) {
                    conn.rollback();
                    return -1;
                }

                try (ResultSet keys = psGrupo.getGeneratedKeys()) {
                    if (keys.next()) {
                        idGrupoGenerado = keys.getInt(1);
                    } else {
                        conn.rollback();
                        return -1;
                    }
                }
            }

            // 2) Insert dueño como miembro del grupo
            try (PreparedStatement psMiembro = conn.prepareStatement(sqlMiembro)) {
                psMiembro.setInt(1, idGrupoGenerado);
                psMiembro.setInt(2, idDueno);
                psMiembro.executeUpdate();
            }

            conn.commit();
            return idGrupoGenerado;

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (Exception ignore) {
                }
                try {
                    conn.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    public ArrayList<GrupoResumenDTO> listarGruposPorUsuario(int idUsuario) throws Exception {

        String sql = "SELECT "
                + " g.id_grupo, "
                + " g.nombre_grupo, "
                + " mg.rol, "
                + " (SELECT COUNT(*) FROM MIEMBRO_GRUPO m2 WHERE m2.id_grupo = g.id_grupo) AS total_miembros "
                + "FROM MIEMBRO_GRUPO mg "
                + "JOIN GRUPO_FAMILIAR g ON mg.id_grupo = g.id_grupo "
                + "WHERE mg.id_usuario = ? "
                + "ORDER BY g.nombre_grupo ASC";

        ArrayList<GrupoResumenDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GrupoResumenDTO dto = new GrupoResumenDTO();
                    dto.setIdGrupo(rs.getInt("id_grupo"));
                    dto.setNombreGrupo(rs.getString("nombre_grupo"));
                    dto.setRol(rs.getString("rol"));
                    dto.setTotalMiembros(rs.getInt("total_miembros"));
                    lista.add(dto);
                }
            }
        }

        return lista;
    }
}
