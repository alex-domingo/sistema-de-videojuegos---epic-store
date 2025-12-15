package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.HealthDTO;
import com.epicstore.epicstore.services.HealthService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/health")
public class HealthController extends HttpServlet {

    private final HealthService healthService = new HealthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HealthDTO dto = healthService.obtenerEstado();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            out.print(gson.toJson(dto));
            out.flush();
        }
    }
}
