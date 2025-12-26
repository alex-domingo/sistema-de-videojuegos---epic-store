package com.epicstore.epicstore.dtos;

public class PerfilUsuarioDTO {

    private String nickname;
    private String pais;
    private String avatar;
    private boolean bibliotecaPublica;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public boolean isBibliotecaPublica() {
        return bibliotecaPublica;
    }

    public void setBibliotecaPublica(boolean bibliotecaPublica) {
        this.bibliotecaPublica = bibliotecaPublica;
    }
}
