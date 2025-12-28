package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.HistorialGastoDTO;
import com.epicstore.epicstore.dtos.ReporteCategoriaFavoritaDTO;
import com.epicstore.epicstore.dtos.ReporteUsoBibliotecaFamiliarDTO;
import com.epicstore.epicstore.dtos.ReporteValoracionBibliotecaDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ReportesUsuarioModel {

    public ArrayList<HistorialGastoDTO> historialGastos(int idUsuario, Date desde, Date hasta) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.fecha_compra, v.titulo, c.precio_unitario AS monto_pagado ")
                .append("FROM COMPRA c ")
                .append("JOIN VIDEOJUEGO v ON v.id_videojuego = c.id_videojuego ")
                .append("WHERE c.id_usuario = ? ");

        if (desde != null) {
            sql.append("AND c.fecha_compra >= ? ");
        }
        if (hasta != null) {
            sql.append("AND c.fecha_compra <= ? ");
        }

        sql.append("ORDER BY c.fecha_compra DESC;");

        ArrayList<HistorialGastoDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setInt(idx++, idUsuario);
            if (desde != null) {
                ps.setDate(idx++, desde);
            }
            if (hasta != null) {
                ps.setDate(idx++, hasta);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HistorialGastoDTO dto = new HistorialGastoDTO();
                    Date f = rs.getDate("fecha_compra");
                    dto.setFechaCompra(f != null ? f.toString() : null);
                    dto.setTitulo(rs.getString("titulo"));
                    dto.setMontoPagado(rs.getDouble("monto_pagado"));
                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error historial gastos: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<ReporteValoracionBibliotecaDTO> valoracionesBiblioteca(int idUsuario) {

        String sql
                = "SELECT v.id_videojuego, v.titulo, "
                + " (SELECT AVG(c2.calificacion) "
                + "  FROM COMENTARIO c2 "
                + "  WHERE c2.id_videojuego = v.id_videojuego "
                + "    AND c2.visible = 'S' "
                + "    AND c2.id_comentario_padre IS NULL) AS promedio_comunidad, "
                + " (SELECT c3.calificacion "
                + "  FROM COMENTARIO c3 "
                + "  WHERE c3.id_videojuego = v.id_videojuego "
                + "    AND c3.id_usuario = ? "
                + "    AND c3.id_comentario_padre IS NULL "
                + "  ORDER BY c3.fecha DESC "
                + "  LIMIT 1) AS valoracion_personal "
                + "FROM BIBLIOTECA_JUEGO b "
                + "JOIN VIDEOJUEGO v ON v.id_videojuego = b.id_videojuego "
                + "WHERE b.id_usuario = ? "
                + "ORDER BY v.titulo ASC;";

        ArrayList<ReporteValoracionBibliotecaDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteValoracionBibliotecaDTO dto = new ReporteValoracionBibliotecaDTO();
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));
                    dto.setTitulo(rs.getString("titulo"));

                    Object prom = rs.getObject("promedio_comunidad");
                    dto.setPromedioComunidad(prom != null ? rs.getDouble("promedio_comunidad") : null);

                    Object pers = rs.getObject("valoracion_personal");
                    dto.setValoracionPersonal(pers != null ? rs.getInt("valoracion_personal") : null);

                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error reporte valoraciones biblioteca: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<ReporteCategoriaFavoritaDTO> categoriasFavoritas(int idUsuario, int limit) {

        // Basado en compras (más sólido que biblioteca)
        String sql
                = "SELECT cat.id_categoria, cat.nombre, COUNT(*) AS total "
                + "FROM COMPRA c "
                + "JOIN VIDEOJUEGO_CATEGORIA vc ON vc.id_videojuego = c.id_videojuego "
                + "JOIN CATEGORIA cat ON cat.id_categoria = vc.id_categoria "
                + "WHERE c.id_usuario = ? "
                + "GROUP BY cat.id_categoria, cat.nombre "
                + "ORDER BY total DESC "
                + "LIMIT ?;";

        ArrayList<ReporteCategoriaFavoritaDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteCategoriaFavoritaDTO dto = new ReporteCategoriaFavoritaDTO();
                    dto.setIdCategoria(rs.getInt("id_categoria"));
                    dto.setNombre(rs.getString("nombre"));
                    dto.setTotal(rs.getInt("total"));
                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error reporte categorías favoritas: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<ReporteUsoBibliotecaFamiliarDTO> usoBibliotecaFamiliar(int idUsuario) {

        String sql
                = "SELECT "
                + "  v.id_videojuego, v.titulo, "
                + "  COALESCE(b.id_grupo_origen, 0) AS id_grupo_origen, "
                + "  b.estado_instalacion, b.fecha_ultimo_cambio_estado, "
                + "  CASE WHEN b.estado_instalacion = 'INSTALADO' THEN 1 ELSE 0 END AS veces_instalado_simulado, "
                + "  CASE "
                + "    WHEN b.estado_instalacion = 'INSTALADO' AND b.fecha_ultimo_cambio_estado IS NOT NULL "
                + "      THEN DATEDIFF(CURDATE(), DATE(b.fecha_ultimo_cambio_estado)) "
                + "    ELSE 0 "
                + "  END AS dias_instalado_simulado, "
                + "  (SELECT AVG(c.calificacion) "
                + "   FROM COMENTARIO c "
                + "   WHERE c.id_videojuego = v.id_videojuego "
                + "     AND c.visible = 'S' "
                + "     AND c.id_comentario_padre IS NULL) AS promedio_comunidad "
                + "FROM BIBLIOTECA_JUEGO b "
                + "JOIN VIDEOJUEGO v ON v.id_videojuego = b.id_videojuego "
                + "WHERE b.id_usuario = ? "
                + "  AND b.tipo_propiedad = 'PRESTADO' "
                + "ORDER BY dias_instalado_simulado DESC, veces_instalado_simulado DESC, v.titulo ASC;";

        ArrayList<ReporteUsoBibliotecaFamiliarDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteUsoBibliotecaFamiliarDTO dto = new ReporteUsoBibliotecaFamiliarDTO();
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));
                    dto.setTitulo(rs.getString("titulo"));
                    dto.setIdGrupoOrigen(rs.getInt("id_grupo_origen"));
                    dto.setEstadoInstalacion(rs.getString("estado_instalacion"));

                    Object fec = rs.getObject("fecha_ultimo_cambio_estado");
                    dto.setFechaUltimoCambioEstado(fec != null ? rs.getTimestamp("fecha_ultimo_cambio_estado").toString() : null);

                    dto.setVecesInstaladoSimulado(rs.getInt("veces_instalado_simulado"));
                    dto.setTiempoInstaladoDiasSimulado(rs.getInt("dias_instalado_simulado"));

                    Object prom = rs.getObject("promedio_comunidad");
                    dto.setPromedioComunidad(prom != null ? rs.getDouble("promedio_comunidad") : null);

                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error reporte uso biblioteca familiar: " + e.getMessage());
        }

        return lista;
    }
}
