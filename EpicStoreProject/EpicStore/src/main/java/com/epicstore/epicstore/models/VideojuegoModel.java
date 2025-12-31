package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.VideojuegoDTO;
import com.epicstore.epicstore.dtos.VideojuegoDetalleDTO;
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

    public VideojuegoDetalleDTO obtenerDetallePorId(int idVideojuego) {

        String sql = "SELECT "
                + " v.id_videojuego, "
                + " v.id_empresa AS id_empresa, "
                + " v.titulo, v.descripcion, v.precio, "
                + " v.requisitos_minimos, v.fecha_lanzamiento, v.imagen_portada, "
                + " e.nombre AS empresa, "
                + " c.codigo AS clasificacion, c.edad_minima, "
                + " IFNULL(AVG(co.calificacion), 0) AS calificacion_promedio "
                + "FROM VIDEOJUEGO v "
                + "JOIN EMPRESA e ON v.id_empresa = e.id_empresa "
                + "JOIN CLASIFICACION_EDAD c ON v.id_clasificacion = c.id_clasificacion "
                + "LEFT JOIN COMENTARIO co ON co.id_videojuego = v.id_videojuego "
                + "WHERE v.id_videojuego = ? "
                + "GROUP BY "
                + " v.id_videojuego, v.id_empresa, v.titulo, v.descripcion, v.precio, "
                + " v.requisitos_minimos, v.fecha_lanzamiento, v.imagen_portada, "
                + " e.nombre, c.codigo, c.edad_minima";

        DBConnection db = new DBConnection();
        VideojuegoDetalleDTO dto = null;

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVideojuego);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                dto = new VideojuegoDetalleDTO();
                dto.setIdVideojuego(rs.getInt("id_videojuego"));
                dto.setIdEmpresa(rs.getInt("id_empresa")); // CLAVE
                dto.setTitulo(rs.getString("titulo"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setPrecio(rs.getDouble("precio"));
                dto.setRequisitosMinimos(rs.getString("requisitos_minimos"));
                dto.setFechaLanzamiento(
                        rs.getDate("fecha_lanzamiento") != null
                        ? rs.getDate("fecha_lanzamiento").toString()
                        : null
                );
                dto.setImagenPortada(rs.getString("imagen_portada"));
                dto.setEmpresa(rs.getString("empresa"));
                dto.setClasificacion(rs.getString("clasificacion"));
                dto.setEdadMinima(rs.getInt("edad_minima"));
                dto.setCalificacionPromedio(rs.getDouble("calificacion_promedio"));
            }

            rs.close();

            // Cargar categorías
            if (dto != null) {
                String sqlCategorias = "SELECT c.nombre "
                        + "FROM VIDEOJUEGO_CATEGORIA vc "
                        + "JOIN CATEGORIA c ON vc.id_categoria = c.id_categoria "
                        + "WHERE vc.id_videojuego = ?";

                try (PreparedStatement psCat = conn.prepareStatement(sqlCategorias)) {
                    psCat.setInt(1, idVideojuego);
                    ResultSet rsCat = psCat.executeQuery();

                    while (rsCat.next()) {
                        dto.getCategorias().add(rsCat.getString("nombre"));
                    }
                    rsCat.close();
                }
            }

        } catch (Exception e) {
            System.err.println("Error al obtener detalle del videojuego: " + e.getMessage());
        }

        return dto;
    }
}
