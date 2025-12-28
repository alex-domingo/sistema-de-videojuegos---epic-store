package com.epicstore.epicstore.services;

import com.epicstore.epicstore.models.ReportesEmpresaModel;

import java.sql.Date;
import java.time.LocalDate;

public class ReportesEmpresaService {

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

    private final ReportesEmpresaModel model = new ReportesEmpresaModel();

    public Resultado ventasPropias(int idEmpresa, String desdeStr, String hastaStr) {
        Date desde = parseFecha(desdeStr);
        Date hasta = parseFecha(hastaStr);

        return new Resultado(true, "Reporte de ventas propias", model.ventasPropias(idEmpresa, desde, hasta));
    }

    public Resultado feedbackCalificaciones(int idEmpresa, String desdeStr, String hastaStr) {
        Date desde = parseFecha(desdeStr);
        Date hasta = parseFecha(hastaStr);
        return new Resultado(true, "Reporte de feedback: calificaciones", model.feedbackCalificaciones(idEmpresa, desde, hasta));
    }

    public Resultado mejoresComentarios(int idEmpresa, String desdeStr, String hastaStr, String limitStr) {
        Date desde = parseFecha(desdeStr);
        Date hasta = parseFecha(hastaStr);
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
        return new Resultado(true, "Reporte de feedback: mejores comentarios", model.mejoresComentarios(idEmpresa, desde, hasta, limit));
    }

    public Resultado peoresCalificaciones(int idEmpresa, String desdeStr, String hastaStr, String limitStr) {
        Date desde = parseFecha(desdeStr);
        Date hasta = parseFecha(hastaStr);
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
        return new Resultado(true, "Reporte de feedback: peores calificaciones", model.peoresCalificaciones(idEmpresa, desde, hasta, limit));
    }

    private Date parseFecha(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        return Date.valueOf(LocalDate.parse(s.trim()));
    }
}
