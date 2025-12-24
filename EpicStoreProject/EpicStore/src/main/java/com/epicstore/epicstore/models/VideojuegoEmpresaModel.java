package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.VideojuegoEmpresaDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class VideojuegoEmpresaModel {

    public ArrayList<VideojuegoEmpresaDTO> listarVideojuegosPorEmpresa(int idEmpresa) {

        String sql
                = "SELECT v.id_videojuego, v.id_empresa, v.id_clasificacion, v.titulo, v.descripcion, v.precio, "
                + "       v.requisitos_minimos, v.fecha_lanzamiento, v.imagen_portada, v.venta_activa, v.comentarios_visibles, "
                + "       ce.codigo AS codigo_clasificacion, ce.edad_minima, "
                + "       (SELECT AVG(c.calificacion) FROM COMENTARIO c WHERE c.id_videojuego = v.id_videojuego AND c.visible = 'S') AS calificacion_promedio, "
                + "       (SELECT COUNT(*) FROM COMPRA co WHERE co.id_videojuego = v.id_videojuego) AS total_ventas "
                + "FROM VIDEOJUEGO v "
                + "JOIN CLASIFICACION_EDAD ce ON ce.id_clasificacion = v.id_clasificacion "
                + "WHERE v.id_empresa = ? "
                + "ORDER BY v.titulo ASC";

        ArrayList<VideojuegoEmpresaDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEmpresa);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VideojuegoEmpresaDTO dto = new VideojuegoEmpresaDTO();
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));
                    dto.setIdEmpresa(rs.getInt("id_empresa"));
                    dto.setIdClasificacion(rs.getInt("id_clasificacion"));
                    dto.setTitulo(rs.getString("titulo"));
                    dto.setDescripcion(rs.getString("descripcion"));
                    dto.setPrecio(rs.getDouble("precio"));
                    dto.setRequisitosMinimos(rs.getString("requisitos_minimos"));
                    dto.setFechaLanzamiento(rs.getDate("fecha_lanzamiento") != null ? rs.getDate("fecha_lanzamiento").toString() : null);
                    dto.setImagenPortada(rs.getString("imagen_portada"));
                    dto.setVentaActiva(rs.getString("venta_activa"));
                    dto.setComentariosVisibles(rs.getString("comentarios_visibles"));

                    dto.setCodigoClasificacion(rs.getString("codigo_clasificacion"));
                    dto.setEdadMinima(rs.getInt("edad_minima"));

                    // AVG puede devolver null si no hay comentarios visibles
                    Object prom = rs.getObject("calificacion_promedio");
                    dto.setCalificacionPromedio(prom != null ? rs.getDouble("calificacion_promedio") : null);

                    dto.setTotalVentas(rs.getInt("total_ventas"));

                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al listar videojuegos de empresa: " + e.getMessage());
        }

        return lista;
    }
}
