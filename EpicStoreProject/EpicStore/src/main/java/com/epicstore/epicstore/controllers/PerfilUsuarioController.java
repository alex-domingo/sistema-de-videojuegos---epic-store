package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.models.PerfilUsuarioModel;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/perfiles/usuario")
public class PerfilUsuarioController extends HttpServlet {

    private final PerfilUsuarioModel model = new PerfilUsuarioModel();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        String nickname = request.getParameter("nickname");

        try (PrintWriter out = response.getWriter()) {

            if (nickname == null || nickname.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", "Falta el parámetro nickname");
                out.print(gson.toJson(salida));
                return;
            }

            try (var conn = model.abrirConexion()) {

                var perfil = model.obtenerPerfilPorNickname(conn, nickname.trim());
                if (perfil == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    salida.put("exito", false);
                    salida.put("mensaje", "Usuario no encontrado");
                    out.print(gson.toJson(salida));
                    return;
                }

                salida.put("exito", true);
                salida.put("usuario", perfil);

                if (perfil.isBibliotecaPublica()) {
                    int idUsuario = model.obtenerIdUsuarioPorNickname(conn, nickname.trim());
                    salida.put("biblioteca", model.bibliotecaPublica(idUsuario));
                } else {
                    salida.put("biblioteca", new Object[0]);
                }

                response.setStatus(HttpServletResponse.SC_OK);
                out.print(gson.toJson(salida));
                out.flush();
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
