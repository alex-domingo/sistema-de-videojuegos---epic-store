package com.epicstore.epicstore.models;

import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RecargaModel {

    public Connection abrirConexion() throws Exception {
        DBConnection db = new DBConnection();
        return db.getConnection();
    }

    public double obtenerSaldoActual(Connection conn, int idUsuario) throws Exception {
        String sql = "SELECT saldo_actual FROM USUARIO WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            // No recargamos a un usuario inexistente, si existe, mostramos saldo_actual
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo_actual");
                }
                throw new Exception("Usuario no existe");
            }
        }
    }

    // 1) Update USUARIO saldo_actual
    public void actualizarSaldo(Connection conn, int idUsuario, double nuevoSaldo) throws Exception {
        String sql = "UPDATE USUARIO SET saldo_actual = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }

    // 2) Insert MOVIMIENTO_SALDO (recarga)
    public void insertarMovimiento(Connection conn, int idUsuario, double monto, double saldoResultante, String descripcion) throws Exception {
        String sql = "INSERT INTO MOVIMIENTO_SALDO "
                + "(id_usuario, tipo, fecha, monto, saldo_resultante, descripcion) "
                + "VALUES (?, 'RECARGA', NOW(), ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setDouble(2, monto);
            ps.setDouble(3, saldoResultante);
            ps.setString(4, descripcion);
            ps.executeUpdate();
        }
    }
}
