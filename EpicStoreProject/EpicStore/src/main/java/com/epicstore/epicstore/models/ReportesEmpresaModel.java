package com.epicstore.epicstore.models;

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
}
