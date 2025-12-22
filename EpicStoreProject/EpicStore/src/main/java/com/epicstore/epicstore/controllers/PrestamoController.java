package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.CrearPrestamoDTO;
import com.epicstore.epicstore.services.PrestamoService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/prestamos")
public class PrestamoController extends HttpServlet {

    private final PrestamoService service = new PrestamoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            CrearPrestamoDTO dto = gson.fromJson(request.getReader(), CrearPrestamoDTO.class);
            PrestamoService.Resultado r = service.prestar(dto);

            if (r.codigo == PrestamoService.OK) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == PrestamoService.GRUPO_NO_EXISTE
                    || r.codigo == PrestamoService.USUARIO_NO_EXISTE
                    || r.codigo == PrestamoService.JUEGO_NO_EXISTE) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == PrestamoService.DUENO_NO_VALIDO
                    || r.codigo == PrestamoService.RECEPTOR_NO_ES_MIEMBRO) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == PrestamoService.RECEPTOR_YA_TIENE_JUEGO) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == PrestamoService.DUENO_NO_TIENE_JUEGO) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
