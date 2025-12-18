package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.ComentarioDTO;
import com.epicstore.epicstore.dtos.CrearComentarioDTO;
import com.epicstore.epicstore.services.ComentarioService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/api/comentarios")
public class ComentarioController extends HttpServlet {

    private final ComentarioService comentarioService = new ComentarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HashMap<String, Object> salida = new HashMap<>();
        Gson gson = new Gson();

        String idParam = request.getParameter("idVideojuego");

        try (PrintWriter out = response.getWriter()) {

            if (idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", "Falta el parámetro idVideojuego");
                out.print(gson.toJson(salida));
                return;
            }

            int idVideojuego = Integer.parseInt(idParam);
            ArrayList<ComentarioDTO> comentarios = comentarioService.listarComentarios(idVideojuego);

            salida.put("exito", true);
            salida.put("mensaje", "Listado de comentarios del videojuego");
            salida.put("total", comentarios.size());
            salida.put("datos", comentarios);

            out.print(gson.toJson(salida));
            out.flush();

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            salida.put("exito", false);
            salida.put("mensaje", "El parámetro idVideojuego debe ser numérico");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            CrearComentarioDTO dto = gson.fromJson(request.getReader(), CrearComentarioDTO.class);

            int resultado = comentarioService.crearComentario(dto);

            if (resultado > 0) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                salida.put("exito", true);
                salida.put("mensaje", "Comentario creado correctamente");
                salida.put("idComentario", resultado);
            } else if (resultado == -2) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", "Calificación inválida (debe ser 1 a 5)");
            } else if (resultado == -3) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                salida.put("exito", false);
                salida.put("mensaje", "No puedes comentar este videojuego porque no lo has comprado");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", "No se pudo crear el comentario");
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
