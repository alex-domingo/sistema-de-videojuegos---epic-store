package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.CompraResultadoDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.*;

public class CompraModel {

    public boolean existeCompra(Connection conn, int idUsuario, int idVideojuego) throws SQLException {
        String sql = "SELECT 1 FROM COMPRA WHERE id_usuario = ? AND id_videojuego = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idVideojuego);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public double obtenerComisionGlobalVigente(Connection conn) throws SQLException {
        String sql = "SELECT porcentaje FROM COMISION_GLOBAL ORDER BY fecha_inicio_vigencia DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("porcentaje");
            }
            return 15.0; // Por si la tabla estuviera vacía
        }
    }

    public CompraResultadoDTO ejecutarCompra(
            int idUsuario,
            int idVideojuego,
            Date fechaCompra,
            double saldoActual,
            double precio,
            double porcentajeComision
    ) throws SQLException {

        DBConnection db = new DBConnection();

        // Cálculos con redondeo a 2 decimales
        double montoComision = redondear2(precio * (porcentajeComision / 100.0));
        double montoEmpresa = redondear2(precio - montoComision);
        double saldoNuevo = redondear2(saldoActual - precio);

        CompraResultadoDTO resultado = new CompraResultadoDTO();
        resultado.setSaldoAnterior(saldoActual);
        resultado.setSaldoNuevo(saldoNuevo);
        resultado.setPrecio(precio);
        resultado.setPorcentajeComision(porcentajeComision);
        resultado.setMontoComision(montoComision);
        resultado.setMontoEmpresa(montoEmpresa);

        Connection conn = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);

            // 1) Validamos no recomprar dentro de la transacción
            if (existeCompra(conn, idUsuario, idVideojuego)) {
                conn.rollback();
                return null; // Indica "ya comprado"
            }

            // 2) Insert COMPRA
            String sqlCompra = "INSERT INTO COMPRA "
                    + "(id_usuario, id_videojuego, fecha_compra, precio_unitario, porcentaje_comision, monto_comision, monto_empresa) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            int idCompraGenerado;

            try (PreparedStatement psCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS)) {
                psCompra.setInt(1, idUsuario);
                psCompra.setInt(2, idVideojuego);
                psCompra.setDate(3, fechaCompra);
                psCompra.setDouble(4, precio);
                psCompra.setDouble(5, porcentajeComision);
                psCompra.setDouble(6, montoComision);
                psCompra.setDouble(7, montoEmpresa);

                int filas = psCompra.executeUpdate();
                if (filas == 0) {
                    conn.rollback();
                    throw new SQLException("No se insertó la compra.");
                }

                try (ResultSet keys = psCompra.getGeneratedKeys()) {
                    if (keys.next()) {
                        idCompraGenerado = keys.getInt(1);
                    } else {
                        conn.rollback();
                        throw new SQLException("No se pudo obtener el id generado de la compra.");
                    }
                }
            }

            resultado.setIdCompra(idCompraGenerado);

            // 3) Insert MOVIMIENTO_SALDO (monto negativo)
            String sqlMov = "INSERT INTO MOVIMIENTO_SALDO "
                    + "(id_usuario, tipo, fecha, monto, saldo_resultante, descripcion) "
                    + "VALUES (?, 'COMPRA', NOW(), ?, ?, ?)";

            try (PreparedStatement psMov = conn.prepareStatement(sqlMov)) {
                psMov.setInt(1, idUsuario);
                psMov.setDouble(2, -precio);
                psMov.setDouble(3, saldoNuevo);
                psMov.setString(4, "Compra de videojuego ID " + idVideojuego);
                psMov.executeUpdate();
            }

            // 4) Update USUARIO saldo_actual
            String sqlUpd = "UPDATE USUARIO SET saldo_actual = ? WHERE id_usuario = ?";
            try (PreparedStatement psUpd = conn.prepareStatement(sqlUpd)) {
                psUpd.setDouble(1, saldoNuevo);
                psUpd.setInt(2, idUsuario);
                int filas = psUpd.executeUpdate();
                if (filas == 0) {
                    conn.rollback();
                    throw new SQLException("No se pudo actualizar el saldo del usuario.");
                }
            }

            // 5) Insert BIBLIOTECA_JUEGO (propio)
            String sqlBib = "INSERT INTO BIBLIOTECA_JUEGO "
                    + "(id_usuario, id_videojuego, id_grupo_origen, tipo_propiedad, estado_instalacion, fecha_ultimo_cambio_estado) "
                    + "VALUES (?, ?, NULL, 'PROPIO', 'NO_INSTALADO', NOW())";

            try (PreparedStatement psBib = conn.prepareStatement(sqlBib)) {
                psBib.setInt(1, idUsuario);
                psBib.setInt(2, idVideojuego);
                psBib.executeUpdate();
            }

            conn.commit();
            return resultado;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignore) {
                }
                try {
                    conn.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }

    private double redondear2(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
