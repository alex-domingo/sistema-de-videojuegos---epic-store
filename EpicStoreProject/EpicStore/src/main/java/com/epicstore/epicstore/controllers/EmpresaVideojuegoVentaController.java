package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.CambiarVentaVideojuegoDTO;
import com.epicstore.epicstore.services.VideojuegoEmpresaService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/empresa/videojuegos/venta")
public class EmpresaVideojuegoVentaController extends HttpServlet {

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

            CambiarVentaVideojuegoDTO dto = gson.fromJson(request.getReader(), CambiarVentaVideojuegoDTO.class);
            VideojuegoEmpresaService.Resultado r = service.cambiarVenta(idEmpresa, dto);

            if (r.exito) {
                response.setStatus(HttpServletResponse.SC_OK);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);
            }

            out.print(gson.toJson(salida));
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
