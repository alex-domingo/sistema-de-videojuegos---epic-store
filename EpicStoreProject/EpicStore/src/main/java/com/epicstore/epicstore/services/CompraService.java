package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.CrearCompraDTO;
import com.epicstore.epicstore.dtos.CompraResultadoDTO;
import com.epicstore.epicstore.models.CompraModel;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;

public class CompraService {

    private final CompraModel compraModel = new CompraModel();

    // Códigos de resultado (para el controller)
    public static final int OK = 1;
    public static final int ERROR_DATOS = -1;
    public static final int NO_EXISTE_USUARIO = -2;
    public static final int NO_EXISTE_JUEGO = -3;
    public static final int JUEGO_NO_EN_VENTA = -4;
    public static final int SALDO_INSUFICIENTE = -5;
    public static final int EDAD_NO_PERMITIDA = -6;
    public static final int YA_COMPRADO = -7;

    public static class ResultadoCompra {

        public int codigo;
        public String mensaje;
        public CompraResultadoDTO data;
    }

    public ResultadoCompra realizarCompra(CrearCompraDTO dto) {

        ResultadoCompra res = new ResultadoCompra();

        if (dto == null || dto.getIdUsuario() <= 0 || dto.getIdVideojuego() <= 0 || dto.getFechaCompra() == null) {
            res.codigo = ERROR_DATOS;
            res.mensaje = "Datos incompletos para realizar la compra";
            return res;
        }

        LocalDate fechaCompra;
        try {
            fechaCompra = LocalDate.parse(dto.getFechaCompra());
        } catch (Exception e) {
            res.codigo = ERROR_DATOS;
            res.mensaje = "fechaCompra inválida (formato esperado yyyy-MM-dd)";
            return res;
        }

        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection()) {

            // 1) Usuario existe + saldo + fecha nacimiento
            double saldoActual;
            LocalDate fechaNacimiento;

            String sqlUsuario = "SELECT saldo_actual, fecha_nacimiento FROM USUARIO WHERE id_usuario = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUsuario)) {
                ps.setInt(1, dto.getIdUsuario());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        res.codigo = NO_EXISTE_USUARIO;
                        res.mensaje = "El usuario no existe";
                        return res;
                    }
                    saldoActual = rs.getDouble("saldo_actual");
                    fechaNacimiento = rs.getDate("fecha_nacimiento").toLocalDate();
                }
            }

            // 2) Juego existe + precio + venta_activa + edad mínima
            double precio;
            String ventaActiva;
            int edadMinima;

            String sqlJuego = "SELECT v.precio, v.venta_activa, c.edad_minima "
                    + "FROM VIDEOJUEGO v "
                    + "JOIN CLASIFICACION_EDAD c ON v.id_clasificacion = c.id_clasificacion "
                    + "WHERE v.id_videojuego = ?";

            try (PreparedStatement ps = conn.prepareStatement(sqlJuego)) {
                ps.setInt(1, dto.getIdVideojuego());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        res.codigo = NO_EXISTE_JUEGO;
                        res.mensaje = "El videojuego no existe";
                        return res;
                    }
                    precio = rs.getDouble("precio");
                    ventaActiva = rs.getString("venta_activa");
                    edadMinima = rs.getInt("edad_minima");
                }
            }

            if (!"S".equalsIgnoreCase(ventaActiva)) {
                res.codigo = JUEGO_NO_EN_VENTA;
                res.mensaje = "El videojuego no está disponible para compra (venta desactivada)";
                return res;
            }

            // 3) Validamos edad mínima (edad al momento de la compra manual)
            int edad = Period.between(fechaNacimiento, fechaCompra).getYears();
            if (edad < edadMinima) {
                res.codigo = EDAD_NO_PERMITIDA;
                res.mensaje = "No cumples la edad mínima para comprar este videojuego";
                return res;
            }

            // 4) Validamos saldo
            if (saldoActual < precio) {
                res.codigo = SALDO_INSUFICIENTE;
                res.mensaje = "Saldo insuficiente para realizar la compra";
                return res;
            }

            // 5) Comisión global vigente
            double comisionVigente = compraModel.obtenerComisionGlobalVigente(conn);

            // 6) Ejecutar compra transaccional (incluimos validación de no recomprar dentro de la transacción)
            CompraResultadoDTO resultado = compraModel.ejecutarCompra(
                    dto.getIdUsuario(),
                    dto.getIdVideojuego(),
                    Date.valueOf(fechaCompra),
                    saldoActual,
                    precio,
                    comisionVigente
            );

            if (resultado == null) {
                res.codigo = YA_COMPRADO;
                res.mensaje = "No se permite recomprar: el usuario ya compró este videojuego";
                return res;
            }

            res.codigo = OK;
            res.mensaje = "Compra realizada correctamente";
            res.data = resultado;
            return res;

        } catch (Exception e) {
            res.codigo = ERROR_DATOS;
            res.mensaje = "Error al procesar la compra: " + e.getMessage();
            return res;
        }
    }
}
