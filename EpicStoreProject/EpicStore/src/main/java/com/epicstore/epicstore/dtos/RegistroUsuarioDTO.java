package com.epicstore.epicstore.dtos;

public class RegistroUsuarioDTO {

    private String nickname;
    private String password;
    private String fechaNacimiento; // yyyy-MM-dd
    private String correo;
    private String telefono;
    private String pais;
    private String avatar;
    private Boolean bibliotecaPublica;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getBibliotecaPublica() {
        return bibliotecaPublica;
    }

    public void setBibliotecaPublica(Boolean bibliotecaPublica) {
        this.bibliotecaPublica = bibliotecaPublica;
    }
}
