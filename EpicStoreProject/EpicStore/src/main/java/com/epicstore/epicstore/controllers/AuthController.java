package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.LoginUsuarioDTO;
import com.epicstore.epicstore.models.AuthModel;
import com.epicstore.epicstore.services.AuthService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/auth/login")
public class AuthController extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            LoginUsuarioDTO dto = gson.fromJson(request.getReader(), LoginUsuarioDTO.class);

            AuthModel.UsuarioLoginData user = authService.loginUsuario(
                    dto != null ? dto.getLogin() : null,
                    dto != null ? dto.getPassword() : null
            );

            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                salida.put("exito", false);
                salida.put("mensaje", "Credenciales inválidas");
                out.print(gson.toJson(salida));
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("tipoSesion", "USUARIO");
            session.setAttribute("idUsuario", user.idUsuario);
            session.setAttribute("nickname", user.nickname);
            session.setAttribute("tipoUsuario", user.tipoUsuario);

            response.setStatus(HttpServletResponse.SC_OK);
            salida.put("exito", true);
            salida.put("mensaje", "Inicio de sesión exitoso");
            salida.put("tipoSesion", "USUARIO");
            salida.put("idUsuario", user.idUsuario);
            salida.put("nickname", user.nickname);
            salida.put("tipoUsuario", user.tipoUsuario);

            out.print(gson.toJson(salida));
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
