package com.epicstore.epicstore.dtos;

public class CambiarVisibilidadComentariosJuegoDTO {

    private Integer idVideojuego;
    private String comentariosVisibles; // "S" o "N"

    public Integer getIdVideojuego() {
        return idVideojuego;
    }

    public void setIdVideojuego(Integer idVideojuego) {
        this.idVideojuego = idVideojuego;
    }

    public String getComentariosVisibles() {
        return comentariosVisibles;
    }

    public void setComentariosVisibles(String comentariosVisibles) {
        this.comentariosVisibles = comentariosVisibles;
    }
}
