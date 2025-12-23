package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.DevolverPrestamoDTO;
import com.epicstore.epicstore.services.DevolucionPrestamoService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/prestamos/devolver")
public class DevolucionPrestamoController extends HttpServlet {

    private final DevolucionPrestamoService service = new DevolucionPrestamoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            DevolverPrestamoDTO dto = gson.fromJson(request.getReader(), DevolverPrestamoDTO.class);
            DevolucionPrestamoService.Resultado r = service.devolver(dto);

            if (r.codigo == DevolucionPrestamoService.OK) {
                response.setStatus(HttpServletResponse.SC_OK);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == DevolucionPrestamoService.GRUPO_NO_EXISTE) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == DevolucionPrestamoService.DUENO_NO_VALIDO) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == DevolucionPrestamoService.PRESTAMO_NO_EXISTE) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
