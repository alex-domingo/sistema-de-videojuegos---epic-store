package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.ReporteCalificacionJuegoDTO;
import com.epicstore.epicstore.dtos.ReporteMejorComentarioDTO;
import com.epicstore.epicstore.dtos.ReportePeorCalificacionDTO;
import com.epicstore.epicstore.dtos.ReporteTopJuegoEmpresaDTO;
import com.epicstore.epicstore.dtos.ReporteVentaPropiaDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ReportesEmpresaModel {

    public ArrayList<ReporteVentaPropiaDTO> ventasPropias(Integer idEmpresa, Date desde, Date hasta) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("  v.id_videojuego, v.titulo, ")
                .append("  COUNT(c.id_compra) AS total_ventas, ")
                .append("  SUM(c.precio_unitario) AS monto_bruto, ")
                .append("  SUM(c.monto_comision) AS monto_comision, ")
                .append("  SUM(c.monto_empresa) AS monto_neto ")
                .append("FROM COMPRA c ")
                .append("JOIN VIDEOJUEGO v ON v.id_videojuego = c.id_videojuego ")
                .append("WHERE v.id_empresa = ? ");

        if (desde != null) {
            sql.append("AND c.fecha_compra >= ? ");
        }
        if (hasta != null) {
            sql.append("AND c.fecha_compra <= ? ");
        }

        sql.append("GROUP BY v.id_videojuego, v.titulo ")
                .append("ORDER BY monto_neto DESC;");

        ArrayList<ReporteVentaPropiaDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setInt(idx++, idEmpresa);

            if (desde != null) {
                ps.setDate(idx++, desde);
            }
            if (hasta != null) {
                ps.setDate(idx++, hasta);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteVentaPropiaDTO dto = new ReporteVentaPropiaDTO();
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));
                    dto.setTitulo(rs.getString("titulo"));
                    dto.setTotalVentas(rs.getInt("total_ventas"));
                    dto.setMontoBruto(rs.getDouble("monto_bruto"));
                    dto.setMontoComision(rs.getDouble("monto_comision"));
                    dto.setMontoNeto(rs.getDouble("monto_neto"));
                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error reporte ventas propias: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<ReporteCalificacionJuegoDTO> feedbackCalificaciones(int idEmpresa, Date desde, Date hasta) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT v.id_videojuego, v.titulo, ")
                .append("       AVG(c.calificacion) AS promedio, ")
                .append("       COUNT(c.id_comentario) AS total_resenas ")
                .append("FROM VIDEOJUEGO v ")
                .append("LEFT JOIN COMENTARIO c ")
                .append("  ON c.id_videojuego = v.id_videojuego ")
                .append(" AND c.visible = 'S' ")
                .append(" AND c.id_comentario_padre IS NULL ")
                .append("WHERE v.id_empresa = ? ");

        if (desde != null) {
            sql.append("AND (c.fecha IS NULL OR c.fecha >= ?) ");
        }
        if (hasta != null) {
            sql.append("AND (c.fecha IS NULL OR c.fecha <= ?) ");
        }

        sql.append("GROUP BY v.id_videojuego, v.titulo ")
                .append("ORDER BY promedio DESC;");

        ArrayList<ReporteCalificacionJuegoDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setInt(idx++, idEmpresa);
            if (desde != null) {
                ps.setDate(idx++, desde);
            }
            if (hasta != null) {
                ps.setDate(idx++, hasta);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteCalificacionJuegoDTO dto = new ReporteCalificacionJuegoDTO();
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));
                    dto.setTitulo(rs.getString("titulo"));

                    Object prom = rs.getObject("promedio");
                    dto.setCalificacionPromedio(prom != null ? rs.getDouble("promedio") : null);

                    dto.setTotalResenas(rs.getInt("total_resenas"));
                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error feedback calificaciones: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<ReporteMejorComentarioDTO> mejoresComentarios(int idEmpresa, Date desde, Date hasta, int limit) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.id_comentario, c.id_videojuego, v.titulo AS titulo_juego, c.texto, c.calificacion, c.fecha, ")
                .append("       (SELECT COUNT(*) FROM COMENTARIO r ")
                .append("        WHERE r.id_comentario_padre = c.id_comentario AND r.visible = 'S') AS total_respuestas ")
                .append("FROM COMENTARIO c ")
                .append("JOIN VIDEOJUEGO v ON v.id_videojuego = c.id_videojuego ")
                .append("WHERE v.id_empresa = ? ")
                .append("  AND c.visible = 'S' ")
                .append("  AND c.id_comentario_padre IS NULL ");

        if (desde != null) {
            sql.append("AND c.fecha >= ? ");
        }
        if (hasta != null) {
            sql.append("AND c.fecha <= ? ");
        }

        sql.append("ORDER BY total_respuestas DESC, c.fecha DESC ")
                .append("LIMIT ?;");

        ArrayList<ReporteMejorComentarioDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setInt(idx++, idEmpresa);
            if (desde != null) {
                ps.setDate(idx++, desde);
            }
            if (hasta != null) {
                ps.setDate(idx++, hasta);
            }
            ps.setInt(idx++, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteMejorComentarioDTO dto = new ReporteMejorComentarioDTO();
                    dto.setIdComentario(rs.getInt("id_comentario"));
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));
                    dto.setTituloJuego(rs.getString("titulo_juego"));
                    dto.setTexto(rs.getString("texto"));

                    Object cal = rs.getObject("calificacion");
                    dto.setCalificacion(cal != null ? rs.getInt("calificacion") : null);

                    Date f = rs.getDate("fecha");
                    dto.setFecha(f != null ? f.toString() : null);

                    dto.setTotalRespuestas(rs.getInt("total_respuestas"));
                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error mejores comentarios: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<ReportePeorCalificacionDTO> peoresCalificaciones(int idEmpresa, Date desde, Date hasta, int limit) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.id_comentario, c.id_videojuego, v.titulo AS titulo_juego, c.calificacion, c.texto, c.fecha ")
                .append("FROM COMENTARIO c ")
                .append("JOIN VIDEOJUEGO v ON v.id_videojuego = c.id_videojuego ")
                .append("WHERE v.id_empresa = ? ")
                .append("  AND c.visible = 'S' ")
                .append("  AND c.id_comentario_padre IS NULL ");

        if (desde != null) {
            sql.append("AND c.fecha >= ? ");
        }
        if (hasta != null) {
            sql.append("AND c.fecha <= ? ");
        }

        sql.append("ORDER BY c.calificacion ASC, c.fecha DESC ")
                .append("LIMIT ?;");

        ArrayList<ReportePeorCalificacionDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setInt(idx++, idEmpresa);
            if (desde != null) {
                ps.setDate(idx++, desde);
            }
            if (hasta != null) {
                ps.setDate(idx++, hasta);
            }
            ps.setInt(idx++, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReportePeorCalificacionDTO dto = new ReportePeorCalificacionDTO();
                    dto.setIdComentario(rs.getInt("id_comentario"));
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));
                    dto.setTituloJuego(rs.getString("titulo_juego"));
                    dto.setCalificacion(rs.getInt("calificacion"));

                    dto.setTexto(rs.getString("texto"));
                    Date f = rs.getDate("fecha");
                    dto.setFecha(f != null ? f.toString() : null);

                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error peores calificaciones: " + e.getMessage());
        }

        return lista;
    }

    public ArrayList<ReporteTopJuegoEmpresaDTO> top5JuegosMasVendidos(int idEmpresa, Date desde, Date hasta) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
                .append("  v.id_videojuego, v.titulo, ")
                .append("  COUNT(c.id_compra) AS total_ventas, ")
                .append("  SUM(c.monto_empresa) AS monto_neto ")
                .append("FROM COMPRA c ")
                .append("JOIN VIDEOJUEGO v ON v.id_videojuego = c.id_videojuego ")
                .append("WHERE v.id_empresa = ? ");

        if (desde != null) {
            sql.append("AND c.fecha_compra >= ? ");
        }
        if (hasta != null) {
            sql.append("AND c.fecha_compra <= ? ");
        }

        sql.append("GROUP BY v.id_videojuego, v.titulo ")
                .append("ORDER BY total_ventas DESC, monto_neto DESC ")
                .append("LIMIT 5;");

        ArrayList<ReporteTopJuegoEmpresaDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setInt(idx++, idEmpresa);
            if (desde != null) {
                ps.setDate(idx++, desde);
            }
            if (hasta != null) {
                ps.setDate(idx++, hasta);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteTopJuegoEmpresaDTO dto = new ReporteTopJuegoEmpresaDTO();
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));
                    dto.setTitulo(rs.getString("titulo"));
                    dto.setTotalVentas(rs.getInt("total_ventas"));
                    dto.setMontoNeto(rs.getDouble("monto_neto"));
                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error reporte top5 juegos: " + e.getMessage());
        }

        return lista;
    }
}
