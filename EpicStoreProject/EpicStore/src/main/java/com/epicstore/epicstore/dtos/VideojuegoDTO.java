package com.epicstore.epicstore.dtos;

public class VideojuegoDTO {

    private int idVideojuego;
    private String titulo;
    private double precio;
    private String imagenPortada;
    private String empresa;
    private String clasificacion;
    private int edadMinima;
    private double calificacionPromedio;

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

    public String getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(String imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }

    public double getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(double calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }
}
