package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.services.UsuarioService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/usuarios/buscar")
public class UsuarioBuscarController extends HttpServlet {

    private final UsuarioService service = new UsuarioService();

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

            var lista = service.buscar(nickname.trim());

            response.setStatus(HttpServletResponse.SC_OK);
            salida.put("exito", true);
            salida.put("total", lista.size());
            salida.put("usuarios", lista);

            out.print(gson.toJson(salida));
            out.flush();
        }
    }
}
