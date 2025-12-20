package com.epicstore.epicstore.dtos;

public class BibliotecaItemDTO {

    private int idBibliotecaJuego;

    private int idVideojuego;
    private String titulo;
    private String imagenPortada;
    private double precio;
    private String empresa;

    private String tipoPropiedad;       // PROPIO / PRESTADO
    private String estadoInstalacion;   // INSTALADO / NO_INSTALADO

    private Integer idGrupoOrigen;      // null si es PROPIO
    private String nombreGrupoOrigen;   // null si es PROPIO

    private String fechaUltimoCambioEstado;

    public int getIdBibliotecaJuego() {
        return idBibliotecaJuego;
    }

    public void setIdBibliotecaJuego(int idBibliotecaJuego) {
        this.idBibliotecaJuego = idBibliotecaJuego;
    }

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

    public String getImagenPortada() {
        return imagenPortada;
    }

    public void setImagenPortada(String imagenPortada) {
        this.imagenPortada = imagenPortada;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTipoPropiedad() {
        return tipoPropiedad;
    }

    public void setTipoPropiedad(String tipoPropiedad) {
        this.tipoPropiedad = tipoPropiedad;
    }

    public String getEstadoInstalacion() {
        return estadoInstalacion;
    }

    public void setEstadoInstalacion(String estadoInstalacion) {
        this.estadoInstalacion = estadoInstalacion;
    }

    public Integer getIdGrupoOrigen() {
        return idGrupoOrigen;
    }

    public void setIdGrupoOrigen(Integer idGrupoOrigen) {
        this.idGrupoOrigen = idGrupoOrigen;
    }

    public String getNombreGrupoOrigen() {
        return nombreGrupoOrigen;
    }

    public void setNombreGrupoOrigen(String nombreGrupoOrigen) {
        this.nombreGrupoOrigen = nombreGrupoOrigen;
    }

    public String getFechaUltimoCambioEstado() {
        return fechaUltimoCambioEstado;
    }

    public void setFechaUltimoCambioEstado(String fechaUltimoCambioEstado) {
        this.fechaUltimoCambioEstado = fechaUltimoCambioEstado;
    }
}
