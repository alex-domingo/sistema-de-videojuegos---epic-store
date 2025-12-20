package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.ActualizarEstadoBibliotecaDTO;
import com.epicstore.epicstore.services.BibliotecaEstadoService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/biblioteca/estado")
public class BibliotecaEstadoController extends HttpServlet {

    private final BibliotecaEstadoService service = new BibliotecaEstadoService();

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            ActualizarEstadoBibliotecaDTO dto = gson.fromJson(request.getReader(), ActualizarEstadoBibliotecaDTO.class);
            BibliotecaEstadoService.Resultado r = service.actualizarEstado(dto);

            if (r.codigo == BibliotecaEstadoService.OK) {
                response.setStatus(HttpServletResponse.SC_OK);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);
            } else if (r.codigo == BibliotecaEstadoService.NO_EXISTE_EN_BIBLIOTECA) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);
            } else if (r.codigo == BibliotecaEstadoService.REGLA_PRESTAMO) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                salida.put("exito", false);
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
            salida.put("exito", false);
            salida.put("mensaje", "JSON inválido o datos incompletos");
        }
    }
}
