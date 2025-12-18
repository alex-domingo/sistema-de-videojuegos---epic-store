package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.VideojuegoDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class VideojuegoModel {

    public ArrayList<VideojuegoDTO> listarVideojuegos() {

        String sql = "SELECT "
                + "    v.id_videojuego, "
                + "    v.titulo, "
                + "    v.precio, "
                + "    v.imagen_portada, "
                + "    e.nombre AS empresa, "
                + "    c.codigo AS clasificacion, "
                + "    c.edad_minima, "
                + "    IFNULL(AVG(co.calificacion), 0) AS calificacion_promedio "
                + "FROM VIDEOJUEGO v "
                + "JOIN EMPRESA e ON v.id_empresa = e.id_empresa "
                + "JOIN CLASIFICACION_EDAD c ON v.id_clasificacion = c.id_clasificacion "
                + "LEFT JOIN COMENTARIO co ON co.id_videojuego = v.id_videojuego "
                + "WHERE v.venta_activa = 'S' "
                + "GROUP BY "
                + "    v.id_videojuego, v.titulo, v.precio, v.imagen_portada, "
                + "    e.nombre, c.codigo, c.edad_minima "
                + "ORDER BY v.titulo ASC";

        ArrayList<VideojuegoDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VideojuegoDTO dto = new VideojuegoDTO();
                dto.setIdVideojuego(rs.getInt("id_videojuego"));
                dto.setTitulo(rs.getString("titulo"));
                dto.setPrecio(rs.getDouble("precio"));
                dto.setImagenPortada(rs.getString("imagen_portada"));
                dto.setEmpresa(rs.getString("empresa"));
                dto.setClasificacion(rs.getString("clasificacion"));
                dto.setEdadMinima(rs.getInt("edad_minima"));
                dto.setCalificacionPromedio(rs.getDouble("calificacion_promedio"));
                lista.add(dto);
            }

        } catch (Exception e) {
            System.err.println("Error al listar videojuegos: " + e.getMessage());
        }

        return lista;
    }
}
