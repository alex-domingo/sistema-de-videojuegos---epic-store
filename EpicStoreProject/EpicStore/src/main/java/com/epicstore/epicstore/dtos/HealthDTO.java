package com.epicstore.epicstore.dtos;

public class HealthDTO {

    private String status;
    private String baseDatos;
    private int totalUsuarios;

    public HealthDTO() {
    }

    public HealthDTO(String status, String baseDatos, int totalUsuarios) {
        this.status = status;
        this.baseDatos = baseDatos;
        this.totalUsuarios = totalUsuarios;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBaseDatos() {
        return baseDatos;
    }

    public void setBaseDatos(String baseDatos) {
        this.baseDatos = baseDatos;
    }

    public int getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(int totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }
}
