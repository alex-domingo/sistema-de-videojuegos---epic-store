package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.HistorialGastoDTO;
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
}
