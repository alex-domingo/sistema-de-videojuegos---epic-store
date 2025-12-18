package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.VideojuegoDTO;
import com.epicstore.epicstore.dtos.VideojuegoDetalleDTO;
import com.epicstore.epicstore.services.VideojuegoService;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/api/videojuegos")
public class VideojuegoController extends HttpServlet {

    private final VideojuegoService videojuegoService = new VideojuegoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HashMap<String, Object> salida = new HashMap<>();
        Gson gson = new Gson();

        try (PrintWriter out = response.getWriter()) {

            if (idParam == null) {
                // LISTADO
                ArrayList<VideojuegoDTO> videojuegos = videojuegoService.obtenerVideojuegos();
                salida.put("exito", true);
                salida.put("mensaje", "Listado de videojuegos disponibles");
                salida.put("total", videojuegos.size());
                salida.put("datos", videojuegos);
            } else {
                // DETALLE
                int idVideojuego = Integer.parseInt(idParam);
                VideojuegoDetalleDTO detalle = videojuegoService.obtenerDetalle(idVideojuego);

                if (detalle == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    salida.put("exito", false);
                    salida.put("mensaje", "Videojuego no encontrado");
                } else {
                    salida.put("exito", true);
                    salida.put("mensaje", "Detalle del videojuego");
                    salida.put("datos", detalle);
                }
            }

            out.print(gson.toJson(salida));
            out.flush();

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            salida.put("exito", false);
            salida.put("mensaje", "El parámetro id debe ser numérico");
        }
    }

}
