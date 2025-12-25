package com.epicstore.epicstore.dtos;

public class VideojuegoPublicoDTO {

    private int idVideojuego;
    private String titulo;
    private double precio;
    private String ventaActiva;
    private Double calificacionPromedio;

    public int getIdVideojuego() {
        return idVideojuego;
    }

    public void setIdVideojuego(int idVideojuego) {
        this.idVideojuego = idVideojuego;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getVentaActiva() {
        return ventaActiva;
    }

    public void setVentaActiva(String ventaActiva) {
        this.ventaActiva = ventaActiva;
    }

    public Double getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(Double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }
}
