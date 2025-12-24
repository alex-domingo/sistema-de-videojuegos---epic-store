package com.epicstore.epicstore.dtos;

public class EliminarMiembroDTO {

    private int idGrupo;
    private int idDueno;
    private int idUsuarioEliminar;

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdDueno() {
        return idDueno;
    }

    public void setIdDueno(int idDueno) {
        this.idDueno = idDueno;
    }

    public int getIdUsuarioEliminar() {
        return idUsuarioEliminar;
    }

    public void setIdUsuarioEliminar(int idUsuarioEliminar) {
        this.idUsuarioEliminar = idUsuarioEliminar;
    }
}
