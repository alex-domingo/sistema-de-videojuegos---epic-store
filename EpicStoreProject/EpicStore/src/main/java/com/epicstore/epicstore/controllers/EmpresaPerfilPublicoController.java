package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.models.EmpresaPublicaModel;
import com.epicstore.epicstore.dtos.EmpresaPublicaDTO;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/empresas/perfil")
public class EmpresaPerfilPublicoController extends HttpServlet {

    private final EmpresaPublicaModel model = new EmpresaPublicaModel();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            String idParam = request.getParameter("idEmpresa");
            if (idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", "Falta parámetro idEmpresa");
                out.print(gson.toJson(salida));
                return;
            }

            int idEmpresa = Integer.parseInt(idParam);

            EmpresaPublicaDTO empresa = model.obtenerEmpresa(idEmpresa);
            if (empresa == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                salida.put("exito", false);
                salida.put("mensaje", "Empresa no encontrada");
                out.print(gson.toJson(salida));
                return;
            }

            salida.put("exito", true);
            salida.put("empresa", empresa);
            salida.put("catalogo", model.listarCatalogo(idEmpresa));

            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(salida));
            out.flush();
        }
    }
}
