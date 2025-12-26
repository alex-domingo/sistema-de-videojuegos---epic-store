package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.RegistroUsuarioDTO;
import com.epicstore.epicstore.services.UsuarioService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/usuarios/registro")
public class UsuarioRegistroController extends HttpServlet {

    private final UsuarioService service = new UsuarioService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            RegistroUsuarioDTO dto = gson.fromJson(request.getReader(), RegistroUsuarioDTO.class);
            UsuarioService.ResultadoRegistro r = service.registrar(dto);

            if (r.codigo == UsuarioService.OK) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);
                salida.put("idUsuario", r.idUsuario);

            } else if (r.codigo == UsuarioService.CORREO_YA_EXISTE || r.codigo == UsuarioService.NICKNAME_YA_EXISTE) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);
            }

            out.print(gson.toJson(salida));
            out.flush();
        }
    }
}
