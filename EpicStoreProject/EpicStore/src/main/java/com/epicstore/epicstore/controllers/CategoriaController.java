package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.CategoriaDTO;
import com.epicstore.epicstore.services.CategoriaService;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.ArrayList;

@WebServlet("/api/categorias")
public class CategoriaController extends HttpServlet {

    private final CategoriaService categoriaService = new CategoriaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<CategoriaDTO> categorias = categoriaService.listarCategorias();

        HashMap<String, Object> salida = new HashMap<>();
        salida.put("exito", true);
        salida.put("mensaje", "Listado de categorías");
        salida.put("total", categorias.size());
        salida.put("datos", categorias);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            out.print(gson.toJson(salida));
            out.flush();
        }
    }
}
