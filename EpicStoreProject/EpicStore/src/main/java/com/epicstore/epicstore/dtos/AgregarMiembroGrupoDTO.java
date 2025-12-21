package com.epicstore.epicstore.dtos;

public class AgregarMiembroGrupoDTO {

    private int idGrupo;
    private int idDueno;         // Quien intenta agregar (debe ser dueño)
    private int idUsuarioNuevo;  // Usuario a agregar

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

    public int getIdUsuarioNuevo() {
        return idUsuarioNuevo;
    }

    public void setIdUsuarioNuevo(int idUsuarioNuevo) {
        this.idUsuarioNuevo = idUsuarioNuevo;
    }
}
