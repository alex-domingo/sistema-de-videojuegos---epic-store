package com.epicstore.epicstore.models;

import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthModel {

    // Login usuario por nickname o correo
    public UsuarioLoginData autenticarUsuario(String login, String password) {

        String sql = "SELECT id_usuario, nickname, tipo_usuario "
                + "FROM USUARIO "
                + "WHERE (nickname = ? OR correo = ?) AND password = ? "
                + "LIMIT 1";

        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, login);
            ps.setString(3, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UsuarioLoginData u = new UsuarioLoginData();
                    u.idUsuario = rs.getInt("id_usuario");
                    u.nickname = rs.getString("nickname");
                    u.tipoUsuario = rs.getString("tipo_usuario"); // COMUN/ADMIN
                    return u;
                }
            }

        } catch (Exception e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }

        return null;
    }

    // Login usuario empresa por correo
    public EmpresaLoginData autenticarUsuarioEmpresa(String correo, String password) {

        String sql = "SELECT id_usuario_empresa, id_empresa, nombre, correo "
                + "FROM USUARIO_EMPRESA "
                + "WHERE correo = ? AND password = ? "
                + "LIMIT 1";

        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmpresaLoginData e = new EmpresaLoginData();
                    e.idUsuarioEmpresa = rs.getInt("id_usuario_empresa");
                    e.idEmpresa = rs.getInt("id_empresa");
                    e.nombre = rs.getString("nombre");
                    e.correo = rs.getString("correo");
                    return e;
                }
            }

        } catch (Exception e) {
            System.err.println("Error al autenticar usuario empresa: " + e.getMessage());
        }

        return null;
    }

    // Clases internas simples para transportar datos
    public static class UsuarioLoginData {

        public int idUsuario;
        public String nickname;
        public String tipoUsuario;
    }

    public static class EmpresaLoginData {

        public int idUsuarioEmpresa;
        public int idEmpresa;
        public String nombre;
        public String correo;
    }
}
