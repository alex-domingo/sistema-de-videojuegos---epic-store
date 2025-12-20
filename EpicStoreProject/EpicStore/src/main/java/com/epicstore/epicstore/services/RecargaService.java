package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.RecargaSaldoDTO;
import com.epicstore.epicstore.models.RecargaModel;

import java.sql.Connection;

public class RecargaService {

    private final RecargaModel model = new RecargaModel();

    public static final int OK = 1;
    public static final int ERROR = -1;
    public static final int MONTO_INVALIDO = -2;
    public static final int USUARIO_NO_EXISTE = -3;

    public static class Resultado {

        public int codigo;
        public String mensaje;
        public double saldoAnterior;
        public double saldoNuevo;
    }

    public Resultado recargar(RecargaSaldoDTO dto) {

        Resultado r = new Resultado();

        // 1) Validamos que el monto a recargar sea > 0 dentro de la transacción
        if (dto == null || dto.getIdUsuario() <= 0 || dto.getMonto() <= 0) {
            r.codigo = MONTO_INVALIDO;
            r.mensaje = "Monto inválido para recarga";
            return r;
        }
        // 2) Validamos transacción si el usuario existe y es un monto válido
        try (Connection conn = model.abrirConexion()) {

            conn.setAutoCommit(false);

            double saldoAnterior = model.obtenerSaldoActual(conn, dto.getIdUsuario());
            double saldoNuevo = Math.round((saldoAnterior + dto.getMonto()) * 100.0) / 100.0;

            model.insertarMovimiento(
                    conn,
                    dto.getIdUsuario(),
                    dto.getMonto(),
                    saldoNuevo,
                    dto.getDescripcion() != null ? dto.getDescripcion() : "Recarga de saldo"
            );

            model.actualizarSaldo(conn, dto.getIdUsuario(), saldoNuevo);

            conn.commit();

            r.codigo = OK;
            r.mensaje = "Recarga realizada correctamente";
            r.saldoAnterior = saldoAnterior;
            r.saldoNuevo = saldoNuevo;
            return r;

        } catch (Exception e) {
            r.codigo = ERROR;
            r.mensaje = "Error al realizar la recarga: " + e.getMessage();
            return r;
        }
    }
}
