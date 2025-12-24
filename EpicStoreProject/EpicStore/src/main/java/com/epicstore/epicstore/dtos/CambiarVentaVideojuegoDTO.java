package com.epicstore.epicstore.dtos;

public class CambiarVentaVideojuegoDTO {

    private Integer idVideojuego;
    private String ventaActiva; // "S" o "N"

    public Integer getIdVideojuego() {
        return idVideojuego;
    }

    public void setIdVideojuego(Integer idVideojuego) {
        this.idVideojuego = idVideojuego;
    }

    public String getVentaActiva() {
        return ventaActiva;
    }

    public void setVentaActiva(String ventaActiva) {
        this.ventaActiva = ventaActiva;
    }
}
