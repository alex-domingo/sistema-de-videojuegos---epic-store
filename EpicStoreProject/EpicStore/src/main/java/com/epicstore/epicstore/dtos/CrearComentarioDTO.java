package com.epicstore.epicstore.dtos;

public class CrearComentarioDTO {

    private int idUsuario;
    private int idVideojuego;
    private Integer idComentarioPadre; // Este comentario puede ser opcional
    private int calificacion;          // 0 <= Calificacion <= 5
    private String texto;              // Puede no contener texto y solo calificación, o en viceversa

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdVideojuego() {
        return idVideojuego;
    }

    public void setIdVideojuego(int idVideojuego) {
        this.idVideojuego = idVideojuego;
    }

    public Integer getIdComentarioPadre() {
        return idComentarioPadre;
    }

    public void setIdComentarioPadre(Integer idComentarioPadre) {
        this.idComentarioPadre = idComentarioPadre;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
