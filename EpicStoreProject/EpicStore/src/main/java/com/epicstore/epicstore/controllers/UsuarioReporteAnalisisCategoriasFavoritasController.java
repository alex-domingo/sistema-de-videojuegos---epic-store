package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.services.ReportesUsuarioService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/reportes/analisis-biblioteca/categorias-favoritas")
public class UsuarioReporteAnalisisCategoriasFavoritasController extends HttpServlet {

    private final ReportesUsuarioService service = new ReportesUsuarioService();

    private Integer obtenerIdUsuarioSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Object tipo = session.getAttribute("tipoSesion");
        if (tipo == null || !"USUARIO".equals(tipo.toString())) {
            return null;
        }

        return (Integer) session.getAttribute("idUsuario");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            Integer idUsuario = obtenerIdUsuarioSesion(request);
            if (idUsuario == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                salida.put("exito", false);
                salida.put("mensaje", "No autenticado como usuario");
                out.print(gson.toJson(salida));
                return;
            }

            String limit = request.getParameter("limit");
            var r = service.categoriasFavoritas(idUsuario, limit);

            response.setStatus(HttpServletResponse.SC_OK);
            salida.put("exito", r.exito);
            salida.put("mensaje", r.mensaje);
            salida.put("datos", r.datos);

            out.print(gson.toJson(salida));
            out.flush();
        }
    }
}
