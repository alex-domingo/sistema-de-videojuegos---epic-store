package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.MovimientoSaldoDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CarteraModel {

    public boolean usuarioExiste(int idUsuario) {
        String sql = "SELECT 1 FROM USUARIO WHERE id_usuario = ? LIMIT 1";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            System.err.println("Error al validar usuario (cartera): " + e.getMessage());
            return false;
        }
    }

    public double obtenerSaldoActual(int idUsuario) {
        String sql = "SELECT saldo_actual FROM USUARIO WHERE id_usuario = ?";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo_actual");
                }
            }

        } catch (Exception e) {
            System.err.println("Error al obtener saldo: " + e.getMessage());
        }

        return 0.0;
    }

    public ArrayList<MovimientoSaldoDTO> listarMovimientos(int idUsuario) {

        String sql = "SELECT id_movimiento, tipo, fecha, monto, saldo_resultante, descripcion "
                + "FROM MOVIMIENTO_SALDO "
                + "WHERE id_usuario = ? "
                + "ORDER BY fecha DESC, id_movimiento DESC";

        ArrayList<MovimientoSaldoDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MovimientoSaldoDTO dto = new MovimientoSaldoDTO();
                    dto.setIdMovimiento(rs.getInt("id_movimiento"));
                    dto.setTipo(rs.getString("tipo"));
                    dto.setFecha(rs.getTimestamp("fecha").toString());
                    dto.setMonto(rs.getDouble("monto"));
                    dto.setSaldoResultante(rs.getDouble("saldo_resultante"));
                    dto.setDescripcion(rs.getString("descripcion"));
                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al listar movimientos: " + e.getMessage());
        }

        return lista;
    }
}
