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

    private Date parseFecha(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        LocalDate ld = LocalDate.parse(s.trim()); // yyyy-MM-dd
        return Date.valueOf(ld);
    }
}
