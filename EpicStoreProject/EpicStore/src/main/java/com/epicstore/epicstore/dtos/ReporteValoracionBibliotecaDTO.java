package com.epicstore.epicstore.dtos;

public class ReporteValoracionBibliotecaDTO {

    private int idVideojuego;
    private String titulo;
    private Double promedioComunidad;
    private Integer valoracionPersonal; // puede ser null

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

    public Double getPromedioComunidad() {
        return promedioComunidad;
    }

    public void setPromedioComunidad(Double promedioComunidad) {
        this.promedioComunidad = promedioComunidad;
    }

    public Integer getValoracionPersonal() {
        return valoracionPersonal;
    }

    public void setValoracionPersonal(Integer valoracionPersonal) {
        this.valoracionPersonal = valoracionPersonal;
    }
}
