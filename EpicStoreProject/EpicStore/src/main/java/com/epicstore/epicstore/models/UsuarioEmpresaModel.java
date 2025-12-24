package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.CrearUsuarioEmpresaDTO;
import com.epicstore.epicstore.dtos.UsuarioEmpresaDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;

public class UsuarioEmpresaModel {

    public ArrayList<UsuarioEmpresaDTO> listarUsuariosEmpresa(int idEmpresa) {
        String sql = "SELECT id_usuario_empresa, id_empresa, nombre, correo, fecha_nacimiento "
                + "FROM USUARIO_EMPRESA WHERE id_empresa = ? ORDER BY nombre ASC";

        ArrayList<UsuarioEmpresaDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UsuarioEmpresaDTO dto = new UsuarioEmpresaDTO();
                    dto.setIdUsuarioEmpresa(rs.getInt("id_usuario_empresa"));
                    dto.setIdEmpresa(rs.getInt("id_empresa"));
                    dto.setNombre(rs.getString("nombre"));
                    dto.setCorreo(rs.getString("correo"));
                    dto.setFechaNacimiento(
                            rs.getDate("fecha_nacimiento") != null
                            ? rs.getDate("fecha_nacimiento").toString()
                            : null
                    );
                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al listar usuarios empresa: " + e.getMessage());
        }

        return lista;
    }

    public boolean existeCorreo(String correo) {
        String sql = "SELECT 1 FROM USUARIO_EMPRESA WHERE correo = ? LIMIT 1";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            System.err.println("Error al validar correo empresa: " + e.getMessage());
            return true; // Conservador
        }
    }

    // 1) INSERT USUARIO_EMPRESA (creamos un nuevo usuario empresa)
    public Integer crearUsuarioEmpresa(int idEmpresa, CrearUsuarioEmpresaDTO dto) {
        String sql = "INSERT INTO USUARIO_EMPRESA (id_empresa, nombre, correo, fecha_nacimiento, password) "
                + "VALUES (?, ?, ?, ?, ?)";

        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idEmpresa);
            ps.setString(2, dto.getNombre());
            ps.setString(3, dto.getCorreo());
            ps.setDate(4, Date.valueOf(dto.getFechaNacimiento()));
            ps.setString(5, dto.getPassword());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                return null;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al crear usuario empresa: " + e.getMessage());
        }

        return null;
    }

    // 2) DELETE USUARIO_EMPRESA (eliminamos a usuario empresa)
    public boolean eliminarUsuarioEmpresa(int idEmpresa, int idUsuarioEmpresa) {
        String sql = "DELETE FROM USUARIO_EMPRESA WHERE id_usuario_empresa = ? AND id_empresa = ?";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuarioEmpresa);
            ps.setInt(2, idEmpresa);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error al eliminar usuario empresa: " + e.getMessage());
            return false;
        }
    }

    public int contarUsuariosEmpresa(int idEmpresa) {
        String sql = "SELECT COUNT(*) AS total FROM USUARIO_EMPRESA WHERE id_empresa = ?";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (Exception e) {
            System.err.println("Error al contar usuarios empresa: " + e.getMessage());
        }

        return 0;
    }
}
