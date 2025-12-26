package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.JuegoPerfilDTO;
import com.epicstore.epicstore.dtos.PerfilUsuarioDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class PerfilUsuarioModel {

    public PerfilUsuarioDTO obtenerPerfilPorNickname(Connection conn, String nickname) throws Exception {

        String sql = "SELECT nickname, pais, avatar, biblioteca_publica, id_usuario "
                + "FROM USUARIO WHERE nickname = ? LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nickname);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                PerfilUsuarioDTO p = new PerfilUsuarioDTO();
                p.setNickname(rs.getString("nickname"));
                p.setPais(rs.getString("pais"));
                p.setAvatar(rs.getString("avatar"));
                p.setBibliotecaPublica("S".equalsIgnoreCase(rs.getString("biblioteca_publica")));
                return p;
            }
        }
    }

    public int obtenerIdUsuarioPorNickname(Connection conn, String nickname) throws Exception {
        String sql = "SELECT id_usuario FROM USUARIO WHERE nickname = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nickname);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
                return -1;
            }
        }
    }

    public ArrayList<JuegoPerfilDTO> bibliotecaPublica(int idUsuario) throws Exception {
        String sql = "SELECT bj.id_videojuego, v.titulo, bj.tipo_propiedad "
                + "FROM BIBLIOTECA_JUEGO bj "
                + "JOIN VIDEOJUEGO v ON bj.id_videojuego = v.id_videojuego "
                + "WHERE bj.id_usuario = ? "
                + "ORDER BY v.titulo ASC";

        ArrayList<JuegoPerfilDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JuegoPerfilDTO j = new JuegoPerfilDTO();
                    j.setIdVideojuego(rs.getInt("id_videojuego"));
                    j.setTitulo(rs.getString("titulo"));
                    j.setTipoPropiedad(rs.getString("tipo_propiedad"));
                    lista.add(j);
                }
            }
        }

        return lista;
    }

    public Connection abrirConexion() throws Exception {
        return new DBConnection().getConnection();
    }
}
