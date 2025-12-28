package com.epicstore.epicstore.services;

import com.epicstore.epicstore.models.ReportesUsuarioModel;

import java.sql.Date;
import java.time.LocalDate;

public class ReportesUsuarioService {

    public static class Resultado {

        public boolean exito;
        public String mensaje;
        public Object datos;

        public Resultado(boolean exito, String mensaje, Object datos) {
            this.exito = exito;
            this.mensaje = mensaje;
            this.datos = datos;
        }
    }

    private final ReportesUsuarioModel model = new ReportesUsuarioModel();

    public Resultado historialGastos(int idUsuario, String desdeStr, String hastaStr) {
        Date desde = parseFecha(desdeStr);
        Date hasta = parseFecha(hastaStr);

        return new Resultado(true, "Historial de gastos", model.historialGastos(idUsuario, desde, hasta));
    }

    private Date parseFecha(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        return Date.valueOf(LocalDate.parse(s.trim()));
    }

    public Resultado valoracionesBiblioteca(int idUsuario) {
        return new Resultado(true, "Análisis de biblioteca: valoraciones", model.valoracionesBiblioteca(idUsuario));
    }

    public Resultado categoriasFavoritas(int idUsuario, String limitStr) {
        int limit = 10;
        if (limitStr != null) {
            try {
                limit = Integer.parseInt(limitStr);
            } catch (Exception ignored) {
            }
            if (limit <= 0) {
                limit = 10;
            }
            if (limit > 50) {
                limit = 50;
            }
        }
        return new Resultado(true, "Análisis de biblioteca: categorías favoritas", model.categoriasFavoritas(idUsuario, limit));
    }
}
