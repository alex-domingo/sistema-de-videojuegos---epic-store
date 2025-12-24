package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.CambiarVisibilidadComentariosJuegoDTO;
import com.epicstore.epicstore.services.VideojuegoEmpresaService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/empresa/videojuegos/comentarios")
public class EmpresaVideojuegosComentariosController extends HttpServlet {

    private final VideojuegoEmpresaService service = new VideojuegoEmpresaService();

    private Integer obtenerIdEmpresaSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object tipo = session.getAttribute("tipoSesion");
        if (tipo == null || !"EMPRESA".equals(tipo.toString())) {
            return null;
        }
        return (Integer) session.getAttribute("idEmpresa");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            Integer idEmpresa = obtenerIdEmpresaSesion(request);
            if (idEmpresa == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                salida.put("exito", false);
                salida.put("mensaje", "No autenticado como empresa");
                out.print(gson.toJson(salida));
                return;
            }

            CambiarVisibilidadComentariosJuegoDTO dto
                    = gson.fromJson(request.getReader(), CambiarVisibilidadComentariosJuegoDTO.class);

            VideojuegoEmpresaService.Resultado r = service.cambiarVisibilidadPorJuego(idEmpresa, dto);

            response.setStatus(r.exito ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            salida.put("exito", r.exito);
            salida.put("mensaje", r.mensaje);

            out.print(gson.toJson(salida));
            out.flush();
        }
    }
}
