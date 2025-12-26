package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.UsuarioBusquedaDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioModel {

    public boolean existeCorreo(Connection conn, String correo) throws Exception {
        String sql = "SELECT 1 FROM USUARIO WHERE correo = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existeNickname(Connection conn, String nickname) throws Exception {
        String sql = "SELECT 1 FROM USUARIO WHERE nickname = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nickname);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int registrarUsuario(String nickname, String password, Date fechaNac, String correo,
            String telefono, String pais, String avatar, boolean bibliotecaPublica) throws Exception {

        DBConnection db = new DBConnection();

        String sql = "INSERT INTO USUARIO (nickname, password, fecha_nacimiento, correo, telefono, pais, avatar, biblioteca_publica) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = db.getConnection()) {
            conn.setAutoCommit(false);

            if (existeCorreo(conn, correo)) {
                return -2;
            }
            if (existeNickname(conn, nickname)) {
                return -3;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nickname);
                ps.setString(2, password);
                ps.setDate(3, fechaNac);
                ps.setString(4, correo);
                ps.setString(5, telefono);
                ps.setString(6, pais);
                ps.setString(7, avatar);
                ps.setString(8, bibliotecaPublica ? "S" : "N");

                int filas = ps.executeUpdate();
                if (filas == 0) {
                    conn.rollback();
                    return -1;
                }

                int id;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return -1;
                    }
                    id = keys.getInt(1);
                }

                conn.commit();
                return id;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                try {
                    conn.setAutoCommit(true);
                } catch (Exception ignore) {
                }
            }
        }
    }

    public ArrayList<UsuarioBusquedaDTO> buscarPorNickname(String filtro) throws Exception {

        String sql = "SELECT nickname, pais, avatar "
                + "FROM USUARIO "
                + "WHERE nickname LIKE ? "
                + "ORDER BY nickname ASC "
                + "LIMIT 20";

        ArrayList<UsuarioBusquedaDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + filtro + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UsuarioBusquedaDTO u = new UsuarioBusquedaDTO();
                    u.setNickname(rs.getString("nickname"));
                    u.setPais(rs.getString("pais"));
                    u.setAvatar(rs.getString("avatar"));
                    lista.add(u);
                }
            }
        }

        return lista;
    }
}
