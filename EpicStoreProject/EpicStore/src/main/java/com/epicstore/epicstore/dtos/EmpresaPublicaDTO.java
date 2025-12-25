package com.epicstore.epicstore.dtos;

public class EmpresaPublicaDTO {

    private int idEmpresa;
    private String nombre;
    private String descripcion;
    private String comentariosVisiblesGlobal;

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentariosVisiblesGlobal() {
        return comentariosVisiblesGlobal;
    }

    public void setComentariosVisiblesGlobal(String comentariosVisiblesGlobal) {
        this.comentariosVisiblesGlobal = comentariosVisiblesGlobal;
    }
}
