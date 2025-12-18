package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.VideojuegoDTO;
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

        ArrayList<VideojuegoDTO> videojuegos = videojuegoService.obtenerVideojuegos();

        HashMap<String, Object> salida = new HashMap<>();
        salida.put("exito", true);
        salida.put("mensaje", "Listado de videojuegos disponibles");
        salida.put("total", videojuegos.size());
        salida.put("datos", videojuegos);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            out.print(gson.toJson(salida));
            out.flush();
        }
    }
}
