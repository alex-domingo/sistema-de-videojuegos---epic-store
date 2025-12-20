package com.epicstore.epicstore.dtos;

public class SesionDTO {

    private boolean autenticado;
    private String tipoSesion; // USUARIO / EMPRESA

    // si tipoSesion = USUARIO
    private Integer idUsuario;
    private String nickname;
    private String tipoUsuario; // COMUN / ADMIN

    // si tipoSesion = EMPRESA
    private Integer idUsuarioEmpresa;
    private Integer idEmpresa;
    private String nombre;
    private String correo;

    public boolean isAutenticado() {
        return autenticado;
    }

    public void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
    }

    public String getTipoSesion() {
        return tipoSesion;
    }

    public void setTipoSesion(String tipoSesion) {
        this.tipoSesion = tipoSesion;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getIdUsuarioEmpresa() {
        return idUsuarioEmpresa;
    }

    public void setIdUsuarioEmpresa(Integer idUsuarioEmpresa) {
        this.idUsuarioEmpresa = idUsuarioEmpresa;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
