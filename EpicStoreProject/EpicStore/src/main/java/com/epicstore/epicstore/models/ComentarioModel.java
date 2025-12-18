package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.ComentarioDTO;
import com.epicstore.epicstore.dtos.CrearComentarioDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ComentarioModel {

    public ArrayList<ComentarioDTO> listarPorVideojuego(int idVideojuego) {

        /*
        Reglas de visibilidad para los comentarios:
        - Empresa: EMPRESA.comentarios_visibles_global
        - Juego: VIDEOJUEGO.comentarios_visibles
        - Comentario: COMENTARIO.visible
        Si cualquiera está en 'N', el texto se oculta (pero devolvemos la calificación) 
         */
        String sql = "SELECT "
                + " co.id_comentario, co.id_videojuego, co.id_usuario, co.id_comentario_padre, "
                + " co.fecha, co.calificacion, co.texto, co.visible AS visible_comentario, "
                + " u.nickname, "
                + " e.comentarios_visibles_global AS visible_empresa, "
                + " v.comentarios_visibles AS visible_juego "
                + "FROM COMENTARIO co "
                + "JOIN USUARIO u ON co.id_usuario = u.id_usuario "
                + "JOIN VIDEOJUEGO v ON co.id_videojuego = v.id_videojuego "
                + "JOIN EMPRESA e ON v.id_empresa = e.id_empresa "
                + "WHERE co.id_videojuego = ? "
                + "ORDER BY co.fecha ASC";

        ArrayList<ComentarioDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVideojuego);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ComentarioDTO dto = new ComentarioDTO();
                    dto.setIdComentario(rs.getInt("id_comentario"));
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));
                    dto.setIdUsuario(rs.getInt("id_usuario"));

                    int padre = rs.getInt("id_comentario_padre");
                    dto.setIdComentarioPadre(rs.wasNull() ? null : padre);

                    dto.setNickname(rs.getString("nickname"));
                    dto.setFecha(rs.getTimestamp("fecha").toString());
                    dto.setCalificacion(rs.getInt("calificacion"));

                    String visibleEmpresa = rs.getString("visible_empresa");
                    String visibleJuego = rs.getString("visible_juego");
                    String visibleComentario = rs.getString("visible_comentario");

                    boolean seMuestraTexto
                            = "S".equalsIgnoreCase(visibleEmpresa)
                            && "S".equalsIgnoreCase(visibleJuego)
                            && "S".equalsIgnoreCase(visibleComentario);

                    dto.setTextoVisible(seMuestraTexto);
                    dto.setTexto(seMuestraTexto ? rs.getString("texto") : null);

                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al listar comentarios: " + e.getMessage());
        }

        return lista;
    }

    public boolean usuarioComproVideojuego(int idUsuario, int idVideojuego) {
        String sql = "SELECT 1 FROM COMPRA WHERE id_usuario = ? AND id_videojuego = ? LIMIT 1";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideojuego);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            System.err.println("Error al validar compra: " + e.getMessage());
            return false;
        }
    }

    public int insertarComentario(CrearComentarioDTO dto) {

        String sql = "INSERT INTO COMENTARIO "
                + "(id_usuario, id_videojuego, id_comentario_padre, fecha, texto, calificacion, visible) "
                + "VALUES (?, ?, ?, NOW(), ?, ?, 'S')";

        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, dto.getIdUsuario());
            ps.setInt(2, dto.getIdVideojuego());

            if (dto.getIdComentarioPadre() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, dto.getIdComentarioPadre());
            }

            ps.setString(4, dto.getTexto());
            ps.setInt(5, dto.getCalificacion());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                return -1;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return -1;

        } catch (Exception e) {
            System.err.println("Error al insertar comentario: " + e.getMessage());
            return -1;
        }
    }

}
