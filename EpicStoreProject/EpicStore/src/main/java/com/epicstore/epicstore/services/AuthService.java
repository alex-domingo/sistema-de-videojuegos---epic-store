package com.epicstore.epicstore.services;

import com.epicstore.epicstore.models.AuthModel;

public class AuthService {

    private final AuthModel authModel = new AuthModel();

    public AuthModel.UsuarioLoginData loginUsuario(String login, String password) {
        if (login == null || password == null) {
            return null;
        }
        if (login.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        return authModel.autenticarUsuario(login.trim(), password);
    }

    public AuthModel.EmpresaLoginData loginEmpresa(String correo, String password) {
        if (correo == null || password == null) {
            return null;
        }
        if (correo.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        return authModel.autenticarUsuarioEmpresa(correo.trim(), password);
    }
}
