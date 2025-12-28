package com.epicstore.epicstore.dtos;

public class ReporteUsoBibliotecaFamiliarDTO {

    private int idVideojuego;
    private String titulo;

    private int idGrupoOrigen; // puede venir 0 si null
    private String estadoInstalacion; // INSTALADO / NO_INSTALADO
    private String fechaUltimoCambioEstado; // DATETIME String

    private int vecesInstaladoSimulado; // 1 si está instalado, 0 si no
    private int tiempoInstaladoDiasSimulado; // si instalado: días desde último cambio; si no: 0

    private Double promedioComunidad; // puede ser null si no hay reseñas visibles

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

    public int getIdGrupoOrigen() {
        return idGrupoOrigen;
    }

    public void setIdGrupoOrigen(int idGrupoOrigen) {
        this.idGrupoOrigen = idGrupoOrigen;
    }

    public String getEstadoInstalacion() {
        return estadoInstalacion;
    }

    public void setEstadoInstalacion(String estadoInstalacion) {
        this.estadoInstalacion = estadoInstalacion;
    }

    public String getFechaUltimoCambioEstado() {
        return fechaUltimoCambioEstado;
    }

    public void setFechaUltimoCambioEstado(String fechaUltimoCambioEstado) {
        this.fechaUltimoCambioEstado = fechaUltimoCambioEstado;
    }

    public int getVecesInstaladoSimulado() {
        return vecesInstaladoSimulado;
    }

    public void setVecesInstaladoSimulado(int vecesInstaladoSimulado) {
        this.vecesInstaladoSimulado = vecesInstaladoSimulado;
    }

    public int getTiempoInstaladoDiasSimulado() {
        return tiempoInstaladoDiasSimulado;
    }

    public void setTiempoInstaladoDiasSimulado(int tiempoInstaladoDiasSimulado) {
        this.tiempoInstaladoDiasSimulado = tiempoInstaladoDiasSimulado;
    }

    public Double getPromedioComunidad() {
        return promedioComunidad;
    }

    public void setPromedioComunidad(Double promedioComunidad) {
        this.promedioComunidad = promedioComunidad;
    }
}
