package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.PublicarVideojuegoDTO;
import com.epicstore.epicstore.dtos.VideojuegoEmpresaDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
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

    public boolean existeClasificacion(int idClasificacion) {
        String sql = "SELECT 1 FROM CLASIFICACION_EDAD WHERE id_clasificacion = ? LIMIT 1";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClasificacion);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("Error al validar clasificación: " + e.getMessage());
            return false;
        }
    }

    public boolean existeTituloEnEmpresa(int idEmpresa, String titulo) {
        String sql = "SELECT 1 FROM VIDEOJUEGO WHERE id_empresa = ? AND LOWER(titulo) = LOWER(?) LIMIT 1";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            ps.setString(2, titulo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("Error al validar título duplicado: " + e.getMessage());
            return true; // conservador
        }
    }

    public Integer publicarVideojuego(int idEmpresa, PublicarVideojuegoDTO dto) {
        String sql
                = "INSERT INTO VIDEOJUEGO ("
                + "  id_empresa, id_clasificacion, titulo, descripcion, precio, requisitos_minimos, "
                + "  fecha_lanzamiento, imagen_portada, venta_activa, comentarios_visibles"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'S', 'S')";

        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idEmpresa);
            ps.setInt(2, dto.getIdClasificacion());
            ps.setString(3, dto.getTitulo());
            ps.setString(4, dto.getDescripcion());
            ps.setDouble(5, dto.getPrecio());
            ps.setString(6, dto.getRequisitosMinimos());

            if (dto.getFechaLanzamiento() == null || dto.getFechaLanzamiento().trim().isEmpty()) {
                ps.setDate(7, null);
            } else {
                ps.setDate(7, Date.valueOf(dto.getFechaLanzamiento()));
            }

            ps.setString(8, dto.getImagenPortada());

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
            System.err.println("Error al publicar videojuego: " + e.getMessage());
        }

        return null;
    }

    public boolean existeVideojuegoEnEmpresa(int idEmpresa, int idVideojuego) {
        String sql = "SELECT 1 FROM VIDEOJUEGO WHERE id_videojuego = ? AND id_empresa = ? LIMIT 1";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVideojuego);
            ps.setInt(2, idEmpresa);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("Error al validar videojuego en empresa: " + e.getMessage());
            return false;
        }
    }

    public boolean existeTituloEnEmpresaExcluyendo(int idEmpresa, String titulo, int idVideojuego) {
        String sql = "SELECT 1 FROM VIDEOJUEGO "
                + "WHERE id_empresa = ? AND LOWER(titulo) = LOWER(?) AND id_videojuego <> ? LIMIT 1";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEmpresa);
            ps.setString(2, titulo);
            ps.setInt(3, idVideojuego);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("Error al validar título duplicado (excluyendo): " + e.getMessage());
            return true; // conservador
        }
    }

    public boolean editarVideojuego(int idEmpresa, int idVideojuego, com.epicstore.epicstore.dtos.EditarVideojuegoDTO dto) {

        String sql
                = "UPDATE VIDEOJUEGO SET "
                + "  id_clasificacion = ?, "
                + "  titulo = ?, "
                + "  descripcion = ?, "
                + "  precio = ?, "
                + "  requisitos_minimos = ?, "
                + "  fecha_lanzamiento = ?, "
                + "  imagen_portada = ? "
                + "WHERE id_videojuego = ? AND id_empresa = ?";

        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dto.getIdClasificacion());
            ps.setString(2, dto.getTitulo());
            ps.setString(3, dto.getDescripcion());
            ps.setDouble(4, dto.getPrecio());
            ps.setString(5, dto.getRequisitosMinimos());

            if (dto.getFechaLanzamiento() == null || dto.getFechaLanzamiento().trim().isEmpty()) {
                ps.setDate(6, null);
            } else {
                ps.setDate(6, java.sql.Date.valueOf(dto.getFechaLanzamiento()));
            }

            ps.setString(7, dto.getImagenPortada());
            ps.setInt(8, idVideojuego);
            ps.setInt(9, idEmpresa);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error al editar videojuego: " + e.getMessage());
            return false;
        }
    }
}
